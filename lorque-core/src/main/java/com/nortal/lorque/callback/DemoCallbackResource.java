package com.nortal.lorque.callback;


import com.nortal.lorque.core.BaseResource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

/**
 * @author Vassili Jakovlev
 */
@Path("/callbacks")
public class DemoCallbackResource extends BaseResource {

  @GET
  public void get(@QueryParam("queryId") Long queryId) {
    System.out.println("GOT CALLBACK FOR QUERY_ID:" + queryId);
  }
}
