package com.nortal.lorque.query;

import com.nortal.lorque.Query;
import com.nortal.lorque.core.BaseResource;
import com.nortal.lorque.plugin.PluginCall;
import com.nortal.lorque.plugin.PluginResult;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

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

  @GET
  @Path("/{id}/results/{pluginName}")
  public Response getPluginResult(@PathParam("id") Long queryId, @PathParam("pluginName") String pluginName) {
    Query query = queryService.getQuery(queryId);
    if (query == null) {
      return Response.status(Response.Status.NOT_FOUND).build();
    }
    Optional<PluginCall> plugin = query.getPlugins().stream().filter(p -> pluginName.equals(p.getName())).findFirst();
    if (!plugin.isPresent()) {
      return Response.status(Response.Status.NOT_FOUND).build();
    }
    PluginResult result = plugin.get().getResult();
    if (result == null) {
      return Response.status(Response.Status.NOT_FOUND).build(); //TODO: or 404?
    }
    final byte[] content = (byte[]) result.getData(); //TODO: add instanceof or meta data in plugin result object

    StreamingOutput streamingOutput = os -> {
      try {
        os.write(content);
        os.flush();
      } catch (IOException e) {
        throw new WebApplicationException(e.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
      }
    };

    Response.ResponseBuilder builder = Response.status(Response.Status.OK);
    return builder.type(getMediaType("foo.pdf")).entity(streamingOutput).build();
  }

  @GET
  @Path("/{id}/report")
  @Deprecated
  public Response downloadReport(@PathParam("id") Long queryId) {
    // Job job = jobService.getJob(jobId);
    // jobService.getReport(jobId);
    final byte[] content = (byte[]) queryService.getQuery(queryId).getPlugins().get(0).getResult().getData();

    if (content == null) {
      throw new WebApplicationException("Job output not found.", Response.Status.NOT_FOUND);
    }

    StreamingOutput streamingOutput = os -> {
      try {
        os.write(content);
        os.flush();
      } catch (IOException e) {
        throw new WebApplicationException(e.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
      }
    };

    Response.ResponseBuilder builder = Response.status(Response.Status.OK);
    return builder.type(getMediaType("foo.pdf")).entity(streamingOutput).build();
  }

  private String getMediaType(String filename) {
    if (filename.toLowerCase().endsWith(".pdf")) {
      return "application/pdf";
    }
    return MediaType.APPLICATION_OCTET_STREAM;
  }

  private class WebsocketCallbackImpl implements WebsocketCallback {

    @Override
    public void broadcast(Object message) {
      Object queryWebsocket = getWebsocket();
      if (queryWebsocket == null) {
        return;
      }
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
