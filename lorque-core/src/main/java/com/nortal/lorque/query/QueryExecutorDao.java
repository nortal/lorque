package com.nortal.lorque.query;

import com.google.gson.JsonArray;
import com.nortal.lorque.Query;
import com.nortal.lorque.QueryParameter;
import com.nortal.lorque.QueryResultSet;
import com.nortal.lorque.core.DataSourceProvider;
import com.nortal.lorque.transformer.ResultSetToJsonUtil;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.*;

/**
 * @author Vassili Jakovlev
 */
public class QueryExecutorDao {

  Map<String, Integer> sqlTypes = new HashMap<>();

  public QueryExecutorDao() {
    for (Field field : Types.class.getFields()) {
      try {
        sqlTypes.put(field.getName(), (Integer) field.get(null));
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      }
    }
  }

  public ArrayList<QueryResultSet> execute(Query query) throws SQLException {
    try (Connection connection = DataSourceProvider.getDataSource().getConnection()) {
      PreparedStatement preparedStatement = connection.prepareStatement(query.getSql());
      int idx = 1;
      setParameters(query, preparedStatement, idx);
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
  }

  private void setParameters(Query query, PreparedStatement preparedStatement, int idx) throws SQLException {
    for (QueryParameter parameter : getParams(query)) {
      preparedStatement.setObject(idx++, parameter.getValue(), getSqlType(parameter.getType()));
    }
  }

  private List<QueryParameter> getParams(Query query) {
    return query.getParameters() == null ? Collections.emptyList() : query.getParameters();
  }

  private int getSqlType(String typeName) {
    Integer type = sqlTypes.get(typeName);
    if (type == null) {
      throw new IllegalArgumentException("Unsupported SQL type: " + typeName);
    }
    return type;
  }

}
