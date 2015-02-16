package com.nortal.lorque.transformer;

import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.nortal.lorque.core.DateFormat;
import org.apache.commons.lang3.tuple.Pair;

import java.math.BigDecimal;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public final class ResultSetToJsonUtil {

  public static JsonObject getRelativeJson(ResultSet rs) throws SQLException {
    JsonObject mainObject = new JsonObject();
    JsonArray titleArray = new JsonArray();
    JsonArray jsonArray = new JsonArray();
    List<Pair<String, Integer>> title = new ArrayList<Pair<String, Integer>>();
    getTitles(rs, titleArray, title);
    mainObject.add("title", titleArray);
    while (rs.next()) {
      JsonArray jsonRow = new JsonArray();
      for (Pair<String, Integer> head : title) {
        addProperty(jsonRow, head.getLeft(), rs, head.getRight());
      }
      jsonArray.add(jsonRow);
    }
    mainObject.add("result_set", jsonArray);
    return mainObject;
  }

  private static void getTitles(ResultSet rs, JsonArray titleArray, List<Pair<String, Integer>> title)
      throws SQLException {
    ResultSetMetaData rsMetaData = rs.getMetaData();
    for (int columnIndex = 1; columnIndex <= rsMetaData.getColumnCount(); columnIndex++) {
      String columnName = rsMetaData.getColumnName(columnIndex);
      int columnType = rsMetaData.getColumnType(columnIndex);
      title.add(Pair.of(columnName, columnType));
      titleArray.add(new JsonPrimitive(columnName));
    }
  }

  public static JsonArray getJson(ResultSet rs) throws SQLException {
    JsonArray jsonArray = new JsonArray();
    ResultSetMetaData rsMetaData = rs.getMetaData();
    while (rs.next()) {
      JsonObject jsonRow = new JsonObject();
      for (int columnIndex = 1; columnIndex <= rsMetaData.getColumnCount(); columnIndex++) {
        String columnName = rsMetaData.getColumnName(columnIndex);
        int columnType = rsMetaData.getColumnType(columnIndex);
        addProperty(jsonRow, columnName, rs, columnType);
      }
      jsonArray.add(jsonRow);
    }
    return jsonArray;
  }

  private static SimpleDateFormat getSimpleDateFormat() {
    return new SimpleDateFormat(DateFormat.ISO_8601); // don't use Locale here!
  }

  private static String formatDate(Object date) {
    return date == null ? null : getSimpleDateFormat().format(date);
  }

  public static void addDatePropertyAsString(JsonArray object, Object date) {
    object.add(new JsonPrimitive(getSimpleDateFormat().format(date)));
  }

  private static void addProperty(JsonObject jsonObject, String propertyName, ResultSet rs, int propertyType)
      throws SQLException {
    switch (propertyType) {
      case Types.VARCHAR:
      case Types.CHAR:
      case Types.LONGNVARCHAR:
      case Types.LONGVARCHAR:
      case Types.NVARCHAR:
        jsonObject.addProperty(propertyName, rs.getString(propertyName));
        break;
      case Types.NUMERIC:
        jsonObject.addProperty(propertyName, rs.getBigDecimal(propertyName));
        break;
      case Types.BIGINT:
      case Types.INTEGER:
      case Types.SMALLINT:
      case Types.TINYINT:
        jsonObject.addProperty(propertyName, getLong(rs.getObject(propertyName)));
        break;
      case Types.DATE:
      case Types.TIMESTAMP:
        jsonObject.addProperty(propertyName, formatDate(rs.getTimestamp(propertyName)));
        break;
    }
  }

  private static Long getLong(Object object) {
    if (object == null) {
      return null;
    } else if (object instanceof BigDecimal) {
      return Long.valueOf(((BigDecimal) object).longValue());
    } else if (object instanceof Long) {
      return (Long) object;
    } else if (object instanceof String) {
      return Long.valueOf((String) object);
    }
    throw new IllegalArgumentException("You can give only String, BigDecimal and Long types!");
  }

  private static void addProperty(JsonArray jsonObject, String propertyName, ResultSet rs, int propertyType)
      throws SQLException {
    switch (propertyType) {
      case Types.VARCHAR:
      case Types.CHAR:
      case Types.LONGNVARCHAR:
      case Types.LONGVARCHAR:
      case Types.NVARCHAR:
        String string = rs.getString(propertyName);
        if (string == null) {
          jsonObject.add(JsonNull.INSTANCE);
        } else {
          jsonObject.add(new JsonPrimitive(string));
        }
        break;
      case Types.NUMERIC:
        BigDecimal bigDecimal = rs.getBigDecimal(propertyName);
        if (bigDecimal == null) {
          jsonObject.add(JsonNull.INSTANCE);
        } else {
          jsonObject.add(new JsonPrimitive(bigDecimal));
        }
        break;
      case Types.BIGINT:
      case Types.INTEGER:
      case Types.SMALLINT:
      case Types.TINYINT:
        Long number = rs.getLong(propertyName);
        if (number == null) {
          jsonObject.add(JsonNull.INSTANCE);
        } else {
          jsonObject.add(new JsonPrimitive(number));
        }
        break;
      case Types.DATE:
      case Types.TIMESTAMP:
        Timestamp timestamp = rs.getTimestamp(propertyName);
        if (timestamp == null) {
          jsonObject.add(JsonNull.INSTANCE);
        } else {
          jsonObject.add(new JsonPrimitive(formatDate(timestamp)));
        }
        break;
    }
  }
}
