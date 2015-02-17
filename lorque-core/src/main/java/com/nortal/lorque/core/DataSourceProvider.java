package com.nortal.lorque.core;

import com.nortal.lorque.osgi.ConfigurationService;
import org.postgresql.ds.PGPoolingDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;

public class DataSourceProvider {

  public static DataSource getDataSource() {
    PGPoolingDataSource dataSource = new PGPoolingDataSource();
    try {
      dataSource.setUrl(ConfigurationService.getString("db.url"));
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    dataSource.setUser(ConfigurationService.getString("db.user"));
    dataSource.setPassword(ConfigurationService.getString("db.password"));
    return dataSource;
  }

}
