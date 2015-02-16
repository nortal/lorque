package com.nortal.lorque.query;

import com.nortal.lorque.*;
import com.nortal.lorque.plugin.LorquePlugin;
import com.nortal.lorque.plugin.PluginCall;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.ServiceProperty;
import org.apache.log4j.Logger;

import javax.ws.rs.client.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Vassili Jakovlev
 */
@Component(name = "QueryService", factoryMethod = "getInstance")
@Instantiate
public class QueryServiceImpl implements QueryService {

  static final Logger log = Logger.getLogger(QueryServiceImpl.class.getName());
  static QueryServiceImpl instance;

  private List<Query> queries = new ArrayList<>();
  private long nextId = 1;

  @Requires(optional = false, specification = LorquePlugin.class)
  private Collection<LorquePlugin> plugins = new ArrayList<>();

  private WebsocketCallback websocketCallback;

  public QueryServiceImpl() {
    new Timer().scheduleAtFixedRate(new TimerTask() {
      @Override
      public void run() {
        refreshPendingQueries();
        executeQueries();
      }
    }, 0, 5000);
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }

  private QueryExecutorDao queryExecutorDao = new QueryExecutorDao();
  private final Queue<Query> pendingQueries = new LinkedList<>();

  @Override
  public List<Query> getActiveQueries() {
    return queries.stream().filter(query -> query.getStatus() != QueryStatus.CANCELLED)
        .map(this::clearResultAndError).collect(Collectors.toList());
  }

  private Query clearResultAndError(Query q) {
    return q;
//    Query clone = ObjectUtils.clone(q);
//    clone.setError(null);
//    clone.setResult(null);
//    return clone;
  }

  @Override
  public Query getQuery(Long id) {
    return queries.stream().filter(query -> query.getId().equals(id)).findFirst().orElse(null);
  }

  @Override
  public void create(Query query) {
    if (query == null || query.getSql() == null) {
      throw new QueryValidationException("Query SQL is missing!");
    }
    query.setSubmitTime(new Date());
    query.setStatus(QueryStatus.SUBMITTED);
    query.setId(nextId++);
    queries.add(query);
    notifyWebsocketClients(query);
  }

  @Override
  public void start(Query query) {
    log.debug("Starting query id='" + query.getId() + "' execution.");
    query.setStatus(QueryStatus.RUNNING);
    query.setStartTime(new Date());
    notifyWebsocketClients(query);
  }

  @Override
  public void complete(Query query) {
    query.setStatus(QueryStatus.COMPLETED);
    query.setEndTime(new Date());
    notifyWebsocketClients(query);
  }

  @Override
  public void fail(Query query) {
    query.setStatus(QueryStatus.FAILED);
    query.setEndTime(new Date());
    notifyWebsocketClients(query);
  }

  @Override
  public void cancel(Query query) {
    query.setStatus(QueryStatus.CANCELLED);
    query.setEndTime(new Date());
    notifyWebsocketClients(query);
  }

  private void notifyWebsocketClients(Query query) {
    if (websocketCallback != null) {
      websocketCallback.broadcast(query);
    }
  }


  private void refreshPendingQueries() {
    List<Query> newQueries = queries.stream().filter(query -> query.getStatus() == QueryStatus.SUBMITTED).collect(Collectors.toList());
    if (!newQueries.isEmpty()) {
      log.debug("Wooohooooo, new queries submitted!");
      synchronized (pendingQueries) {
        pendingQueries.clear();
        pendingQueries.addAll(newQueries);
      }
    }
    if (pendingQueries.isEmpty()) {
      log.debug("No queries to process. Sleeping...");
    } else {
      log.debug("Total number of pending queries: " + pendingQueries.size());
    }
  }

  private void executeQueries() {
    synchronized (pendingQueries) {
      while (!pendingQueries.isEmpty()) {
        executeQuery(pendingQueries.poll());
      }
    }
  }

  private void executeQuery(Query query) {
    Runnable executor = () -> {
      start(query);
      try {
        ArrayList<QueryResultSet> result = queryExecutorDao.execute(query);
        query.setResult(result);
        executePlugins(query);
        complete(query);
        notifyCallback(query);
      } catch (Exception e) {
        QueryError error = new QueryError();
        error.setMessage(ExceptionUtils.getMessage(e));
        error.setContent(ExceptionUtils.getStackTrace(e));
        query.setError(error);
        fail(query);
      }
    };
    executor.run();
  }

  private void executePlugins(Query query) {
    if (query.getPlugins() == null) {
      return;
    }
    query.getPlugins().forEach(p -> executePlugin(query, p));
  }

  private void executePlugin(Query query, PluginCall pluginCall) {
    String pluginName = pluginCall.getName();
    LorquePlugin plugin = getPlugin(pluginName);
    if (plugin == null) {
      query.setError(new QueryError("No plugin '" + pluginName + "' registered.", null));
      return;
    }
    plugin.execute(query, pluginCall);
  }

  private LorquePlugin getPlugin(String pluginName) {
    return plugins.stream().filter(p -> StringUtils.equals(pluginName, p.getName())).findFirst().orElse(null);
  }

  private void notifyCallback(Query query) {
    ExecutionCallback callback = query.getCallback();
    if (callback == null || callback.getMethod() == null || callback.getUrl() == null) {
      return;
    }
    Client client = ClientBuilder.newClient();
    WebTarget target = client.target(callback.getUrl());
    target = target.queryParam("queryId", query.getId());

    Invocation.Builder builder = target.request();
    switch (callback.getMethod()) {
      case "POST":
        builder.post(Entity.json("Foobar")); // FIXME
        break;
      case "GET":
        builder.get();
        break;
    }
  }

  @Override
  public void setWebsocketCallback(WebsocketCallback callback) {
    this.websocketCallback = callback;
  }

  public synchronized static QueryService getInstance() {
    if (instance == null) {
      instance = new QueryServiceImpl();
    }
    return instance;
  }

}