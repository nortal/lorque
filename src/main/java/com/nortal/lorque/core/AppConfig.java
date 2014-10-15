package com.nortal.lorque.core;

import com.nortal.lorque.query.QueryResource;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.hsqldb.jdbc.JDBCDriver;
import org.postgresql.ds.PGPoolingDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.sql.DataSource;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import javax.ws.rs.ext.RuntimeDelegate;
import java.sql.SQLException;

/**
 * @author Vassili Jakovlev
 */
@Configuration
@PropertySource(value = "classpath:runtime.properties")
@ComponentScan(basePackages = {"com.nortal.lorque"})
@EnableScheduling
public class AppConfig {

  @Autowired
  private Environment env;

  @Bean
  public DataSource dataSource() {
    PGPoolingDataSource dataSource = new PGPoolingDataSource();
    try {
      dataSource.setUrl(env.getProperty("db.url"));
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    dataSource.setUser(env.getProperty("db.user"));
    dataSource.setPassword(env.getProperty("db.password"));
    return dataSource;
  }

  @Bean(name = "jdbcTemplate")
  public JdbcTemplate jdbcTemplate() {
    JdbcTemplate jdbcTemplate = new JdbcTemplate();
    jdbcTemplate.setDataSource(dataSource());
    return jdbcTemplate;
  }

  @Bean
  public DataSource internalDataSource() {
    BasicDataSource dataSource = new BasicDataSource();
    dataSource.setDriver(new JDBCDriver());
    dataSource.setUrl("jdbc:hsqldb:mem:cacheMem");
    dataSource.setUsername("sa");
    dataSource.setConnectionProperties("sql.syntax_ora=TRUE;");
    dataSource.setPoolPreparedStatements(true);
    return dataSource;
  }

  @Bean
  public JdbcTemplate internalJdbcTemplate() {
    JdbcTemplate internalJdbcTemplate = new JdbcTemplate();
    internalJdbcTemplate.setDataSource(internalDataSource());
    return internalJdbcTemplate;
  }

  @Bean(destroyMethod = "shutdown", name = "cxf")
  public SpringBus cxf() {
    return new SpringBus();
  }

  @Bean
  @DependsOn("cxf")
  public Server jaxRsServer(ApplicationContext appContext) {
    JAXRSServerFactoryBean factory = RuntimeDelegate.getInstance().createEndpoint(jaxRsApiApplication(), JAXRSServerFactoryBean.class);
    factory.setServiceBeanObjects(queryResource());
    factory.setAddress("/" + factory.getAddress());
    factory.setProvider(gsonProvider());
    return factory.create();
  }


  @Bean
  public JaxRsApiApplication jaxRsApiApplication() {
    return new JaxRsApiApplication();
  }

  @ApplicationPath("/")
  public class JaxRsApiApplication extends Application {
  }

  @Bean
  public GsonProvider gsonProvider() {
    return new GsonProvider();
  }

  @Bean
  public QueryResource queryResource() {
    return new QueryResource();
  }

}
