package com.nortal.lorque.plugin.rengy;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.cm.ManagedService;
import org.osgi.util.tracker.ServiceTracker;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;

/**
 * @author Vassili Jakovlev (vassili.jakovlev@nortal.com)
 */
public class Activator implements BundleActivator {

  public static final String PID = "com.nortal.lorque.plugin.rengy";

  public void start(BundleContext context) throws Exception {
    new ConfigurationServiceTracker(context).open();
    Hashtable<String, Object> properties = new Hashtable<>();
    properties.put(Constants.SERVICE_PID, PID);
    context.registerService(ManagedService.class, new ConfigurationService(), properties);
  }

  public void stop(BundleContext context) throws Exception {
//    httpTracker.close();
  }

  private class ConfigurationServiceTracker extends ServiceTracker<ConfigurationAdmin, ConfigurationAdmin> {

    public ConfigurationServiceTracker(BundleContext context) {
      super(context, ConfigurationAdmin.class.getName(), null);
    }

    @Override
    public ConfigurationAdmin addingService(ServiceReference<ConfigurationAdmin> reference) {
      ConfigurationAdmin configurationAdmin = context.getService(reference);
      try {
        Configuration configuration = configurationAdmin.getConfiguration(PID);
        Dictionary properties = new Hashtable<>();
        properties.put("rengy.url", "http://localhost:8080/rengy");
        properties.put("rengy.timeout", 30000);
        configuration.update(properties);
      } catch (IOException e) {
        e.printStackTrace();
      }
      return super.addingService(reference);
    }
  }


}
