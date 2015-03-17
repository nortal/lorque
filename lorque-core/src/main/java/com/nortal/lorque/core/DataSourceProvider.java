package com.nortal.lorque.core;

import com.nortal.lorque.osgi.Activator;
import com.nortal.lorque.osgi.ConfigurationService;
import oracle.jdbc.pool.OracleDataSource;
import org.postgresql.ds.PGPoolingDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.AbstractCollection;

public class DataSourceProvider {

  public static DataSource getDataSource() {
    String url = ConfigurationService.getString(Activator.PID, "db.url");
    String username = ConfigurationService.getString(Activator.PID, "db.user");
    String password = ConfigurationService.getString(Activator.PID, "db.password");
    if (url.contains("postgresql")) {
      return getPostgreDataSoure(url, username, password);
    } else if (url.contains("oracle")) {
      return getOracleDataSource(url, username, password);
    }
    throw new IllegalArgumentException("Unknown database.");
  }

  private static DataSource getPostgreDataSoure(String url, String username, String password) {
    PGPoolingDataSource dataSource = new PGPoolingDataSource();
    try {
      dataSource.setUrl(url);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    dataSource.setUser(username);
    dataSource.setPassword(password);
    return dataSource;
  }

  private static DataSource getOracleDataSource(String url, String username, String password) {
    OracleDataSource dataSource;
    try {
      dataSource = new OracleDataSource();
      dataSource.setURL(url);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    dataSource.setUser(username);
    dataSource.setPassword(password);
    return dataSource;
  }

}
