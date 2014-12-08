package com.nortal.lorque.query;

import com.google.gson.JsonArray;

import java.util.Date;
import java.util.List;

/**
 * @author Vassili Jakovlev
 */
public class Query {

  private Long id;
  private QueryStatus status;
  private String querySql;
  private List<String> queryParameters;
  private Date submitTime;
  private Date startTime;
  private Date endTime;
  private JsonArray result;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public QueryStatus getStatus() {
    return status;
  }

  public void setStatus(QueryStatus status) {
    this.status = status;
  }

  public String getQuerySql() {
    return querySql;
  }

  public void setQuerySql(String querySql) {
    this.querySql = querySql;
  }

  public List<String> getQueryParameters() {
    return queryParameters;
  }

  public void setQueryParameters(List<String> queryParameters) {
    this.queryParameters = queryParameters;
  }

  public Date getSubmitTime() {
    return submitTime;
  }

  public void setSubmitTime(Date submitTime) {
    this.submitTime = submitTime;
  }

  public Date getStartTime() {
    return startTime;
  }

  public void setStartTime(Date startTime) {
    this.startTime = startTime;
  }

  public Date getEndTime() {
    return endTime;
  }

  public void setEndTime(Date endTime) {
    this.endTime = endTime;
  }

  public JsonArray getResult() {
    return result;
  }

  public void setResult(JsonArray result) {
    this.result = result;
  }
}
