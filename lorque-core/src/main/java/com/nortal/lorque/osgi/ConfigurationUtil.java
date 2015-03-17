package com.nortal.lorque.osgi;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class ConfigurationUtil {

  public static Dictionary<String, String> loadFromFile(String pid) throws IOException {
    File file = getPropertyFile(pid);
    Hashtable<String, String> properties = new Hashtable<>();
    if (!file.exists()) {
      return properties;
    }
    LineIterator lineIterator = FileUtils.lineIterator(file, "UTF-8");
    while (lineIterator.hasNext()) {
      String[] keyValue = lineIterator.nextLine().split("=");
      if (keyValue.length > 1) {
        properties.put(keyValue[0], keyValue[1]);
      }
    }
    return properties;
  }

  public static void saveToFile(Dictionary<String, ?> properties, String pid) {
    File file = getPropertyFile(pid);
    List<String> formattedProperties = new ArrayList<>();
    Enumeration<String> keys = properties.keys();
    while (keys.hasMoreElements()) {
      String key = keys.nextElement();
      if ("service.pid".equals(key)) {
        continue;
      }
      String value = (String) properties.get(key);
      formattedProperties.add(key + "=" + StringUtils.defaultString(value));
    }
    try {
      FileUtils.writeLines(file, formattedProperties);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static void addDefaultProperty(Dictionary<String, String> properties, String key, String value) {
    if (properties.get(key) != null) {
      return;
    }
    properties.put(key, value);
  }

  private static File getPropertyFile(String pid) {
    String confDir = System.getProperty("user.home") + File.separator + ".lorque" + File.separator + "conf";
    return new File(confDir + File.separator + pid + ".conf");
  }

}
