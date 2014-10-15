package com.nortal.lorque.query;

import com.nortal.lorque.transformer.ResultSetToJsonUtil;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Vassili Jakovlev
 */
@Repository
public class QueryExecutorDao {

  @Resource(name = "jdbcTemplate")
  private JdbcTemplate jdbcTemplate;

  public String execute(Query query) {
    return jdbcTemplate.query(query.getQuerySql(), new ResultSetExtractor<String>() {

      @Override
      public String extractData(ResultSet rs) throws SQLException, DataAccessException {
        try {
          String json = ResultSetToJsonUtil.getJson(rs);
          return json;
        } catch (SQLException e) {
          throw e;
        }
      }

    });
  }

}
