package com.nortal.lorque;

import com.nortal.lorque.plugin.PluginCall;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Vassili Jakovlev
 */
public class Query implements Serializable {

  private Long id;
  private QueryStatus status;
  private String sql;
  private List<QueryParameter> parameters;
  private List<PluginCall> plugins;
  private ExecutionCallback callback;
  private Date submitTime;
  private Date startTime;
  private Date endTime;
  private List<QueryResultSet> result;
  private QueryError error;

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

  public String getSql() {
    return sql;
  }

  public void setSql(String sql) {
    this.sql = sql;
  }

  public List<QueryParameter> getParameters() {
    return parameters;
  }

  public void setParameters(List<QueryParameter> parameters) {
    this.parameters = parameters;
  }

  public List<PluginCall> getPlugins() {
    return plugins;
  }

  public void setPlugins(List<PluginCall> plugins) {
    this.plugins = plugins;
  }

  public ExecutionCallback getCallback() {
    return callback;
  }

  public void setCallback(ExecutionCallback callback) {
    this.callback = callback;
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

  public List<QueryResultSet> getResult() {
    return result;
  }

  public void setResult(List<QueryResultSet> result) {
    this.result = result;
  }

//  public void setPluginResult(String pluginName, Object result) {
//    if (pluginResults == null) {
//      pluginResults = new HashMap<>();
//    }
//    pluginResults.put(pluginName, result);
//  }

  public QueryError getError() {
    return error;
  }

  public void setError(QueryError error) {
    this.error = error;
  }

}
