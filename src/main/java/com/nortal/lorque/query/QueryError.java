package com.nortal.lorque.query;

/**
 * @author Vassili Jakovlev (vassili.jakovlev@nortal.com)
 */
public class QueryError {

  private String message;
  private String content;

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }
}
