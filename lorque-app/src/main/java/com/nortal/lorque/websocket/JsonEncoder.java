package com.nortal.lorque.websocket;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class JsonEncoder implements Encoder.Text<Object> {

  private Gson gson;

  @Override
  public void init(final EndpointConfig config) {
    if (gson == null) {
      gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssX").create();
    }
  }

  @Override
  public String encode(Object object) throws EncodeException {
    return gson.toJson(object);
  }


  @Override
  public void destroy() {
  }

}
