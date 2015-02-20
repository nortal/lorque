package com.nortal.lorque.osgi;

import com.nortal.lorque.callback.DemoCallbackResource;
import com.nortal.lorque.core.GsonProvider;
import com.nortal.lorque.query.QueryResource;
import com.nortal.lorque.settings.SettingsResource;
import com.nortal.lorque.ui.UiResource;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.transport.servlet.CXFNonSpringServlet;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;
import org.osgi.util.tracker.ServiceTracker;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;

/**
 * @author Vassili Jakovlev (vassili.jakovlev@nortal.com)
 */
public class Activator implements BundleActivator {

  public static final String PID = "com.nortal.lorque";
  private HttpServiceTracker httpTracker;

  public void start(BundleContext context) throws Exception {
    httpTracker = new HttpServiceTracker(context);
    httpTracker.open();
    new ConfigurationServiceTracker(context).open();
    Hashtable<String, Object> properties = new Hashtable<>();
    properties.put(Constants.SERVICE_PID, PID);
    context.registerService(ManagedService.class, new ConfigurationService(), properties);
  }

  public void stop(BundleContext context) throws Exception {
    httpTracker.close();
  }

  private class HttpServiceTracker extends ServiceTracker<HttpService, HttpService> {

    public HttpServiceTracker(BundleContext context) {
      super(context, HttpService.class.getName(), null);
    }

    CXFNonSpringServlet cxf;

    public HttpService addingService(ServiceReference<HttpService> reference) {
      HttpService httpService = context.getService(reference);
      try {
        System.setProperty("javax.ws.rs.ext.RuntimeDelegate", "org.apache.cxf.jaxrs.impl.RuntimeDelegateImpl");
        cxf = new CXFNonSpringServlet();
        httpService.registerServlet("/api/v1", cxf, null, null);
        JAXRSServerFactoryBean factory = new JAXRSServerFactoryBean();
        factory.setBus(cxf.getBus());
        factory.setServiceBeanObjects(new QueryResource(), new SettingsResource(), new DemoCallbackResource(), UiResource.getInstance());
        factory.setProvider(new GsonProvider<>());
        factory.setAddress("/");
        factory.create();
      } catch (ServletException | NamespaceException e) {
        e.printStackTrace(); // FIXME
      }
      return httpService;
    }

    public void removedService(ServiceReference<HttpService> reference, HttpService service) {
      try {
        service.unregister("/api/v1");
        if (cxf != null) {
          cxf.destroy();
        }
      } catch (IllegalArgumentException e) {
        e.printStackTrace(); // FIXME
      }
    }
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
        properties.put("db.url", "jdbc:postgresql://delex.webmedia.int:5432/nortalsystem");
        properties.put("db.user", "");
        properties.put("db.password", "");
        configuration.update(properties);
      } catch (IOException e) {
        e.printStackTrace();
      }
      return super.addingService(reference);
    }
  }


}
