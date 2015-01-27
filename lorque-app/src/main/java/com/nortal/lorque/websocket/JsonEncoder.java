package com.nortal.lorque.websocket;

import com.google.gson.Gson;
import com.nortal.lorque.core.GsonProvider;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class JsonEncoder implements Encoder.Text<Object> {

  private Gson gson;

  @Override
  public void init(final EndpointConfig config) {
    gson = GsonProvider.getGsonBuilder().create();
  }

  @Override
  public String encode(Object object) throws EncodeException {
    return gson.toJson(object);
  }


  @Override
  public void destroy() {
  }

}
