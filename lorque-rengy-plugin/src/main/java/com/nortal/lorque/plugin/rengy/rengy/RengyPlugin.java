package com.nortal.lorque.plugin.rengy.rengy;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.nortal.lorque.Query;
import com.nortal.lorque.plugin.LorquePlugin;
import com.nortal.lorque.plugin.PluginCall;
import eu.rengy.client.Report;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.ServiceProperty;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component(name = "Rengy plugin", managedservice = "com.nortal.lorque.plugin.rengy")
@Provides
@Instantiate
public class RengyPlugin implements LorquePlugin {

  @ServiceProperty(name = "rengy.url")
  private String rengyUrl;
  @ServiceProperty(name = "rengy.timeout")
  private int rengyTimeout;

  @Override
  public String getName() {
    return "rengy";
  }

  @Override
  public void execute(Query query, PluginCall pluginCall) {
    byte[] bytes = generateReport(pluginCall);
    pluginCall.setResult(bytes);
  }

  public byte[] generateReport(PluginCall pluginCall) {
//    String rengyUrl = ConfigurationService.getString("rengy.url");

    Gson gson = new GsonBuilder().create();
    Report report = gson.fromJson(pluginCall.getParameters().toString(), Report.class);

    JsonArray properties = new JsonArray();
    for (Map.Entry<String, String> entry : System.getenv().entrySet()) {
      JsonObject property = new JsonObject();
      property.addProperty("key", entry.getKey());
      property.addProperty("value", entry.getValue());
      properties.add(property);
    }
    JsonObject baseItem = new JsonObject();
    baseItem.add("items", properties);

    Map<String, String> data = new HashMap<>();
    data.put("BASE_ITEM", baseItem.toString());
    report.setData(data);

    PostMethod method = report.post(rengyUrl + "/main", rengyTimeout);
    try {
      return method.getResponseBody();
    } catch (IOException e) {
      return null;
    }
  }
}
