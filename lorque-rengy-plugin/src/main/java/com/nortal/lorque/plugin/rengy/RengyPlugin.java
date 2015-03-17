package com.nortal.lorque.plugin.rengy;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nortal.lorque.Query;
import com.nortal.lorque.QueryResultSet;
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
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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

    List<QueryResultSet> resultSets = query.getResult();

    Map<String, String> data = report.getData() != null ? report.getData() : new HashMap<>();

    data.putAll(data.entrySet()
        .stream()
        .collect(Collectors.toMap(Map.Entry::getKey,
            e -> replaceReferences(e.getValue(), resultSets, gson))));

    report.setData(data);

    PostMethod method = report.post(rengyUrl + "/main", rengyTimeout);
    try {
      return method.getResponseBody();
    } catch (IOException e) {
      return null;
    }
  }

  private String replaceReferences(String data, List<QueryResultSet> resultSets, Gson gson) {
    Pattern pattern = Pattern.compile("\"@resultsets\\[(\\d+)\\]\"", Pattern.CASE_INSENSITIVE);
    String input = data;
    Matcher matcher = pattern.matcher(input);
    while (matcher.find()) {
      int idx = Integer.valueOf(matcher.group(1));
      input = matcher.replaceFirst(gson.toJsonTree(resultSets.get(idx).getData()).toString());
      matcher = pattern.matcher(input);
    }
    return input;
  }

}
