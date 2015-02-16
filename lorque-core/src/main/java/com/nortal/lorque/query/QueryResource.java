package com.nortal.lorque.query;

import com.nortal.lorque.Query;
import com.nortal.lorque.core.BaseResource;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author Vassili Jakovlev
 */
@Path("/queries")
@Produces(MediaType.APPLICATION_JSON)
public class QueryResource extends BaseResource {

  private QueryService queryService = QueryServiceImpl.getInstance();

  public QueryResource() {
    queryService.setWebsocketCallback(new WebsocketCallbackImpl());
  }

  @GET
  public List<Query> getActiveQueries() {
    return queryService.getActiveQueries();
  }

  @GET
  @Path("/{id}")
  public Query get(@PathParam("id") Long id) {
    Query query = queryService.getQuery(id);
    if (query == null) {
      throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).entity("Query not found.").build());
    }
    return query;
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  public Response create(Query query) {
    queryService.create(query);
    return created(query.getId());
  }

  @DELETE
  @Path("/{id}")
  public void cancel(@PathParam("id") Long id) {
    Query query = queryService.getQuery(id);
    if (query == null) {
      throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).entity("Query not found.").build());
    }
    queryService.cancel(query);
  }

  private class WebsocketCallbackImpl implements WebsocketCallback {

    @Override
    public void broadcast(Object message) {
      Object queryWebsocket = getWebsocket();
      try {
        // FIXME: that's dirty hack because of different classloaders
        Method method = queryWebsocket.getClass().getMethod("broadcast", Object.class);
        method.invoke(queryWebsocket, message);
      } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
        e.printStackTrace();
      }
    }

    private Object getWebsocket() {
      try {
        return new InitialContext().lookup("queryWebsocket");
      } catch (NamingException e) {
        e.printStackTrace();
      }
      return null;
    }

  }

}
