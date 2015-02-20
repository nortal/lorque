package com.nortal.lorque.ui;

import com.nortal.lorque.core.BaseResource;
import com.nortal.lorque.plugin.CustomColumn;
import com.nortal.lorque.plugin.impl.PluginService;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Requires;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Vassili Jakovlev
 */
@Path("/ui")
@Produces(MediaType.APPLICATION_JSON)
@Component(name = "UiService", factoryMethod = "getInstance")
@Instantiate
public class UiResource extends BaseResource {

  private static UiResource instance;

  @Requires(optional = false)
  private PluginService pluginService;

  @GET
  @Path("/columns")
  public List<CustomColumn> getPluginColumns() {
    Map<String, List<CustomColumn>> columns = new LinkedHashMap<>();
    return pluginService.getPlugins().stream().filter(p -> p.getCustomColumn() != null)
        .map(p -> p.getCustomColumn()).collect(Collectors.toList());
  }

  public synchronized static UiResource getInstance() {
    if (instance == null) {
      instance = new UiResource();
    }
    return instance;
  }

}
