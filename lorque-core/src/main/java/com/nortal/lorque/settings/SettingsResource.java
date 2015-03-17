package com.nortal.lorque.settings;

import com.nortal.lorque.osgi.ConfigurationService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.osgi.service.cm.Configuration;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.*;

@Path("/settings")
@Produces(MediaType.APPLICATION_JSON)
public class SettingsResource {

  @GET
  public List<Pair<String, String>> getSettings() {
    List<Pair<String, String>> settings = new ArrayList<>();
    Dictionary<String, ?> properties = ConfigurationService.getAllProperties();
    getSettings(settings, properties);
    return settings;
  }

  private void getSettings(List<Pair<String, String>> settings, Dictionary<String, ?> properties) {
    Enumeration<String> keys = properties.keys();
    while (keys.hasMoreElements()) {
      String key = keys.nextElement();
      String value = properties.get(key).toString();
      if (key.contains("pass")) {
        value = StringUtils.repeat("*", new Random().nextInt(10) + 3);
      }
      settings.add(Pair.of(key, value));
    }
  }

}
