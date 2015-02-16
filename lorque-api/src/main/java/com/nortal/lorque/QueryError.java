package com.nortal.lorque;

/**
 * @author Vassili Jakovlev
 */
public class QueryError {

  private String message;
  private String content;

  public QueryError() {
  }

  public QueryError(String message, String content) {
    this.message = message;
    this.content = content;
  }

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
