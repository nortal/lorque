package com.nortal.lorque;

import java.io.Serializable;


/**
 * @author Vassili Jakovlev
 */
public class ExecutionCallback implements Serializable {
  private String url;
  private String method;

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getMethod() {
    return method;
  }

  public void setMethod(String method) {
    this.method = method;
  }
}
