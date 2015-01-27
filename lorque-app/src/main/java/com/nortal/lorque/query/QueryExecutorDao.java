package com.nortal.lorque.query;

import com.google.gson.JsonArray;
import com.nortal.lorque.Query;
import com.nortal.lorque.QueryResultSet;
import com.nortal.lorque.transformer.ResultSetToJsonUtil;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author Vassili Jakovlev
 */
@Repository
public class QueryExecutorDao {

  @Resource(name = "jdbcTemplate")
  private JdbcTemplate jdbcTemplate;

  public ArrayList<QueryResultSet> execute(Query query) throws SQLException {
    Connection connection = jdbcTemplate.getDataSource().getConnection();
    PreparedStatement preparedStatement = connection.prepareStatement(query.getSql());
    boolean hasResults = preparedStatement.execute();
    // TODO: return update count as row count
    ArrayList<QueryResultSet> result = new ArrayList<>();
    while (hasResults) {
      ResultSet resultSet = preparedStatement.getResultSet();
      JsonArray resultSetData = ResultSetToJsonUtil.getJson(resultSet);
      result.add(new QueryResultSet(resultSetData));
      hasResults = preparedStatement.getMoreResults();
    }
    return result;
  }

  private Object[] getParams(Query query) {
    return query.getParameters() == null ? new Object[]{} : query.getParameters().toArray();
  }

}
