package com.nortal.lorque.plugin.rengy;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import eu.rengy.client.Report;
import eu.rengy.client.TemplateFile;
import org.apache.commons.httpclient.methods.PostMethod;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.client.*;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Path("callback")
public class LorqueCallbackResource {

  //  @GET
  public Response callback() {
    System.out.println("Callback");
//    String data = "{\"format\":\"PDF\",\"locale\":\"en\",\"operation\":\"DATA\",\"cachePath\":[\"rengy\",\"demo\",\"data-json\"],\"templateFiles\":[{\"filename\":\"json-data.rptdesign\",\"url\":\"http://localhost:8080/rengy-demo/examples/json-data.rptdesign\",\"isBase\":true}],\"data\":{\"COMPANY_ITEM\":\"{\"name\":\"NORTAL\",\"workers\":[{\"id\":1,\"code\":\"P1\",\"name\":\"Anton Stalnuhhin\"},{\"id\":2,\"code\":\"P2\",\"name\":\"Sergei Tsimbalist\"},{\"id\":3,\"code\":\"P3\",\"name\":\"Vassili Jakovlev\"},{\"id\":4,\"code\":\"P4\",\"name\":\"Igor Bossenko\"},{\"id\":5,\"code\":\"P5\",\"name\":\"Max Boiko\"},{\"id\":6,\"code\":\"P6\",\"name\":\"Daniel Dubrovski\"}]}\"}}";
    String data = "{\"format\": \"PDF\", \"locale\": \"en\", \"operation\": \"DATA\", \"cachePath\": [ \"rengy\", \"demo\", \"data-json\" ], \"templateFiles\": [ { \"filename\": \"json-data.rptdesign\", \"url\": \"http://localhost:8080/rengy-demo/examples/json-data/json-data.rptdesign\", \"isBase\": true } ], \"data\": { \"COMPANY_ITEM\": \"{\\\"name\\\":\\\"NORTAL\\\",\\\"workers\\\":[{\\\"name\\\":\\\"Anton Stalnuhhin\\\"},{\\\"name\\\":\\\"Sergei Tsimbalist\\\"}]}\" }}";
    Client client = ClientBuilder.newClient();
    WebTarget target = client.target("http://localhost:8080/rengy/main?uncompressed=true");
//    target = target.queryParam("queryId", query.getId());

    Invocation.Builder builder = target.request();
//    builder.accept(MediaType.APPLICATION_JSON_TYPE);
    Response response = builder.post(Entity.json(data));
    return response;
  }

  @GET
  public Response foo() throws IOException {
    String url = "http://localhost:8080";
    String format = Report.FORMAT__PDF;
    String rengyUrl = url + "/rengy";

    Report report = new Report();
    report.setFormat(format);
    report.setCachePath(Arrays.asList("rengy", "demo", "server-to-server"));

    TemplateFile file = new TemplateFile();
    file.setFilename("json-data.rptdesign");
    file.setUrl(url + "/rengy-demo/examples/server-to-server.rptdesign");
    file.setIsBase(true);
    report.setTemplateFiles(Arrays.asList(file));

    JsonArray properties = new JsonArray();
    for (Map.Entry<String, String> entry : System.getenv().entrySet()) {
      JsonObject property = new JsonObject();
      property.addProperty("key", entry.getKey());
      property.addProperty("value", entry.getValue());
      properties.add(property);
    }
    JsonObject baseItem = new JsonObject();
    baseItem.add("items", properties);

    Map<String, String> data = new HashMap<String, String>();
    data.put("BASE_ITEM", baseItem.toString());
    report.setData(data);

    PostMethod method = report.post(rengyUrl + "/main", 30000);
    return Response.ok(method.getResponseBody()).type("application/pdf").build();
//    org.apache.commons.io.IOUtils.copy(method.getResponseBodyAsStream(), response.getOutputStream());
  }
}
