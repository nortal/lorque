package com.nortal.lorque.osgi;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;

import java.util.*;

public class ConfigurationService implements ManagedService {

  private static Map<String, Dictionary<String, ?>> properties = new HashMap<>();
  private final String pid;

  public ConfigurationService(String pid) {
    this.pid = pid;
  }

  @Override
  public void updated(Dictionary<String, ?> properties) throws ConfigurationException {
    ConfigurationService.properties.put(pid, properties);
    ConfigurationUtil.saveToFile(properties, pid);
  }

  public static Dictionary<String, ?> getProperties(String pid) {
    return properties.get(pid);
  }

  public static Dictionary<String, ?> getAllProperties() {
    Dictionary<String, Object> allProps = new Hashtable<>();
    for (Map.Entry<String, Dictionary<String, ?>> entry : properties.entrySet()) {
      Dictionary<String, ?> props = entry.getValue();
      Enumeration<String> keys = props.keys();
      while (keys.hasMoreElements()) {
        String key = keys.nextElement();
        if ("service.pid".equals(key)) {
          continue;
        }
        allProps.put(key, props.get(key));
      }
    }
    return allProps;
  }

  public static String getString(String pid, String key) {
    return (String) getProperties(pid).get(key);
  }


}
