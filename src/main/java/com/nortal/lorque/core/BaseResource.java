package com.nortal.lorque.core;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author Vassili Jakovlev
 */
@Produces(MediaType.APPLICATION_JSON)
public class BaseResource {

  @Context
  private UriInfo uriInfo;

  protected Response created(Object id) {
    try {
      URI location = new URI(uriInfo.getAbsolutePathBuilder().path(id.toString()).build().toString());
      return Response.created(location).build();
    } catch (URISyntaxException e) {
      throw new WebApplicationException(e);
    }
  }

}
