package com.nortal.lorque.settings;

import com.nortal.lorque.plugin.LorquePlugin;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@Path("/settings")
@Produces(MediaType.APPLICATION_JSON)
public class SettingsResource {

  @GET
  public List<Pair<String, String>> getSettings() {
    List<Pair<String, String>> settings = new ArrayList<>();
    settings.add(getProp("db.url"));
    settings.add(getProp("db.user"));
    return settings;
  }

  private Pair<String, String> getProp(String key) {
    return Pair.of(key, "foo");
  }

}
