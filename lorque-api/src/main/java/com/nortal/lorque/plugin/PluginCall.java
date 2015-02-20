package com.nortal.lorque.plugin;

/**
 * @author Vassili Jakovlev
 */
public class PluginCall {

  private String name;
  private Object parameters;
  private PluginResult result;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Object getParameters() {
    return parameters;
  }

  public void setParameters(Object parameters) {
    this.parameters = parameters;
  }

  public PluginResult getResult() {
    return result;
  }

  public void setResult(PluginResult result) {
    this.result = result;
  }
}
