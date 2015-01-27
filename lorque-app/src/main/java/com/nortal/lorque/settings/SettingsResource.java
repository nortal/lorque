package com.nortal.lorque.settings;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@Component
@Path("/settings")
@Produces(MediaType.APPLICATION_JSON)
public class SettingsResource {

  @Autowired
  private Environment env;

  @GET
  public List<Pair<String, String>> getSettings() {
    List<Pair<String, String>> settings = new ArrayList<>();
    settings.add(getProp("db.url"));
    settings.add(getProp("db.user"));
    return settings;
  }

  private Pair<String, String> getProp(String key) {
    return Pair.of(key, env.getProperty(key));
  }

}
