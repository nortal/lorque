package com.nortal.lorque.query;

import com.nortal.lorque.core.BaseResource;

import javax.annotation.Resource;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * @author Vassili Jakovlev
 */
@Path("queries")
public class QueryResource extends BaseResource {

  @Resource
  private QueryService queryService;

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
    queryService.cancel(id);
  }

}
