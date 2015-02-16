package com.nortal.lorque.plugin.rengy;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;

import java.util.Dictionary;

public class ConfigurationService implements ManagedService {

  private static Dictionary<String, ?> properties;

  @Override
  public void updated(Dictionary<String, ?> properties) throws ConfigurationException {
    ConfigurationService.properties = properties;
  }

  public static Dictionary<String, ?> getProperties() {
    return properties;
  }

  public static String getString(String key) {
    return (String) properties.get(key);
  }
}
