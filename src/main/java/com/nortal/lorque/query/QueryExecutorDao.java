package com.nortal.lorque.query;

import com.google.gson.JsonArray;
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

  public JsonArray execute(Query query) throws SQLException {
    return jdbcTemplate.query(query.getQuerySql(), new ResultSetExtractor<JsonArray>() {

      @Override
      public JsonArray extractData(ResultSet rs) throws SQLException, DataAccessException {
        return ResultSetToJsonUtil.getJson(rs);
      }

    }, getParams(query));
  }

  private Object[] getParams(Query query) {
    return query.getQueryParameters() == null ? new Object[]{} : query.getQueryParameters().toArray();
  }

}
