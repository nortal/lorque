package com.nortal.lorque.core;

/**
 * @author Vassili Jakovlev
 */
//@Configuration
//@PropertySource(value = {"file:///${user.home}/.lorque/db.properties"}, ignoreResourceNotFound = true)
//@ComponentScan(basePackages = {"com.nortal.lorque"})
//@EnableScheduling
//@EnableAsync
public class AppConfig {

//  @Autowired
//  private Environment env;
//
//  @Bean
//  public DataSource dataSource() {
//    PGPoolingDataSource dataSource = new PGPoolingDataSource();
//    try {
//      dataSource.setUrl(env.getProperty("db.url"));
//    } catch (SQLException e) {
//      throw new RuntimeException(e);
//    }
//    dataSource.setUser(env.getProperty("db.user"));
//    dataSource.setPassword(env.getProperty("db.password"));
//    return dataSource;
//  }
//
//  @Bean(name = "jdbcTemplate")
//  public JdbcTemplate jdbcTemplate() {
//    JdbcTemplate jdbcTemplate = new JdbcTemplate();
//    jdbcTemplate.setDataSource(dataSource());
//    return jdbcTemplate;
//  }
//
//  @Bean
//  public DataSource internalDataSource() {
//    BasicDataSource dataSource = new BasicDataSource();
//    dataSource.setDriver(new JDBCDriver());
//    dataSource.setUrl("jdbc:hsqldb:mem:cacheMem");
//    dataSource.setUsername("sa");
//    dataSource.setConnectionProperties("sql.syntax_ora=TRUE;");
//    dataSource.setPoolPreparedStatements(true);
//    return dataSource;
//  }
//
//  @Bean
//  public JdbcTemplate internalJdbcTemplate() {
//    JdbcTemplate internalJdbcTemplate = new JdbcTemplate();
//    internalJdbcTemplate.setDataSource(internalDataSource());
//    return internalJdbcTemplate;
//  }
//
//  @Bean(destroyMethod = "shutdown", name = "cxf")
//  public SpringBus cxf() {
//    return new SpringBus();
//  }
//
//  @Bean
//  @DependsOn("cxf")
//  public Server jaxRsServer(ApplicationContext appContext) {
//    JAXRSServerFactoryBean factory = RuntimeDelegate.getInstance().createEndpoint(jaxRsApiApplication(), JAXRSServerFactoryBean.class);
//    factory.setServiceBeanObjects(queryResource(), settingsResource(), callbackResource());
//    factory.setAddress("/" + factory.getAddress());
//    factory.setProvider(gsonProvider());
//    return factory.create();
//  }
//
//
//  @Bean
//  public JaxRsApiApplication jaxRsApiApplication() {
//    return new JaxRsApiApplication();
//  }
//
//  @ApplicationPath("/")
//  public class JaxRsApiApplication extends Application {
//  }
//
//  @Bean
//  public GsonProvider gsonProvider() {
//    return new GsonProvider();
//  }
//
//  @Bean
//  public QueryResource queryResource() {
//    return new QueryResource();
//  }
//
//  @Bean
//  public SettingsResource settingsResource() {
//    return new SettingsResource();
//  }
//
//  @Bean
//  public DemoCallbackResource callbackResource() {
//    return new DemoCallbackResource();
//  }

}
