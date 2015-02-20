package com.nortal.lorque.plugin;

/**
 * @author Vassili Jakovlev
 */
public class PluginResult {

  private Object data;
  private String mediaType;

  public PluginResult() {
  }

  public PluginResult(Object data, String mediaType) {
    this.data = data;
    this.mediaType = mediaType;
  }

  public String getMediaType() {
    return mediaType;
  }

  public void setMediaType(String mediaType) {
    this.mediaType = mediaType;
  }

  public Object getData() {
    return data;
  }

  public void setData(Object data) {
    this.data = data;
  }
}
