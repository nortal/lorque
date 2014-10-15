package com.nortal.lorque.query;

import com.nortal.lorque.core.BaseResource;

import javax.annotation.Resource;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
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

}
