package com.nortal.lorque.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * @author Vassili Jakovlev
 */
@Provider
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
public class GsonProvider<T> implements MessageBodyReader<T>, MessageBodyWriter<T> {


  private final Gson gson;

  public GsonProvider() {
    gson = getGsonBuilder().create();
  }

  public static GsonBuilder getGsonBuilder() {
    return new GsonBuilder().setDateFormat(DateFormat.ISO_8601);
  }

  @Override
  public long getSize(T t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
    return -1;
  }

  @Override
  public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
    return true;
  }

  @Override
  public void writeTo(T object,
                      Class<?> type,
                      Type genericType,
                      Annotation[] annotations,
                      MediaType mediaType,
                      MultivaluedMap<String, Object> httpHeaders,
                      OutputStream entityStream) throws IOException, WebApplicationException {
    PrintWriter printWriter = new PrintWriter(entityStream);
    String json;
    if (object instanceof String) {
      json = (String) object;
    } else {
      json = gson.toJson(object);
    }
    try {
      printWriter.write(json);
      printWriter.flush();
    } finally {
      printWriter.close();
    }
  }

  @Override
  public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
    return true;
  }

  @Override
  public T readFrom(Class<T> type,
                    Type gnericType,
                    Annotation[] annotations,
                    MediaType mediaType,
                    MultivaluedMap<String, String> httpHeaders,
                    InputStream entityStream) throws IOException, WebApplicationException {
    InputStreamReader reader = new InputStreamReader(entityStream, "UTF-8");
    try {
      return gson.fromJson(reader, type);
    } finally {
      reader.close();
    }
  }
}