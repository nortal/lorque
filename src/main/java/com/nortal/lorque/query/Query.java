package com.nortal.lorque.query;

import java.util.Date;

/**
 * @author Vassili Jakovlev
 */
public class Query {

  private Long id;
  private QueryStatus status;
  private String querySql;
  private String queryParameters;
  private Date submitTime;
  private Date startTime;
  private Date endTime;
  private String result;

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

  public String getQueryParameters() {
    return queryParameters;
  }

  public void setQueryParameters(String queryParameters) {
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

  public String getResult() {
    return result;
  }

  public void setResult(String result) {
    this.result = result;
  }
}
