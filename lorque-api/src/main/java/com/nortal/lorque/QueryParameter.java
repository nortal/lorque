package com.nortal.lorque;

/**
 * @author Vassili Jakovlev
 */
public class QueryParameter {

  private String type; // See java.sql.Types for type names
  private String value;

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }
}
