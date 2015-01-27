package com.nortal.lorque;

import com.google.gson.JsonArray;

/**
 * @author Vassili Jakovlev
 */
public class QueryResultSet {

  private JsonArray data;
  private int rowCount;

  public QueryResultSet() {
  }

  public QueryResultSet(JsonArray data) {
    this.data = data;
    if (data != null) {
      this.rowCount = data.size();
    }
  }

  public JsonArray getData() {
    return data;
  }

  public void setData(JsonArray data) {
    this.data = data;
  }

  public int getRowCount() {
    return rowCount;
  }

  public void setRowCount(int rowCount) {
    this.rowCount = rowCount;
  }
}
