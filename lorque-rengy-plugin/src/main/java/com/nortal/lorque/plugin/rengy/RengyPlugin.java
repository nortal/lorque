package com.nortal.lorque.plugin.rengy;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.nortal.lorque.Query;
import com.nortal.lorque.plugin.CustomColumn;
import com.nortal.lorque.plugin.LorquePlugin;
import com.nortal.lorque.plugin.PluginCall;
import com.nortal.lorque.plugin.PluginResult;
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
    byte[] bytes = generateReport(query, pluginCall);
    pluginCall.setResult(new PluginResult(bytes, "application/pdf"));
  }

  @Override
  public CustomColumn getCustomColumn() {
    CustomColumn column = new CustomColumn();
    column.setTitle("Rengy PDF");
    column.setContent("<a href='../api/v1/queries/:queryId/report'>PDF</a>");
    column.setPluginName(getName());
    return column;
  }

  public byte[] generateReport(Query query, PluginCall pluginCall) {
    Gson gson = new GsonBuilder().create();
    Report report = gson.fromJson(pluginCall.getParameters().toString(), Report.class);

    JsonObject baseItem = new JsonObject();
    baseItem.add("items", gson.toJsonTree(query.getResult().get(0).getData()));

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
