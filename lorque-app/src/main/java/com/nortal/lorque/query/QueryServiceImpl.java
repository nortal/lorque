package com.nortal.lorque.query;

import com.nortal.lorque.*;
import com.nortal.lorque.websocket.QueryWebsocket;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.ws.rs.client.*;
import java.util.*;

/**
 * @author Vassili Jakovlev
 */
@Service("queryService")
public class QueryServiceImpl implements QueryService {

  static final Logger log = LogManager.getLogger(QueryServiceImpl.class.getName());

  @Resource
  private QueryDao queryDao;
  @Resource
  private QueryExecutorDao queryExecutorDao;

  public final Queue<Query> pendingQueries = new LinkedList<>();

  @Override
  public List<Query> getActiveQueries() {
    return queryDao.getActiveQueries();
  }

  @Override
  public Query getQuery(Long id) {
    return queryDao.getQuery(id);
  }

  @Override
  public void create(Query query) {
    if (query == null || query.getSql() == null) {
      throw new QueryValidationException("Query SQL is missing!");
    }
    query.setSubmitTime(new Date());
    query.setStatus(QueryStatus.SUBMITTED);
    Long id = queryDao.create(query);
    query.setId(id);
    QueryWebsocket.broadcast(query);
  }

  @Override
  public void start(Query query) {
    log.debug("Starting query id='" + query.getId() + "' execution.");
    query.setStatus(QueryStatus.RUNNING);
    query.setStartTime(new Date());
    updateAndNotify(query);
  }

  @Override
  public void complete(Query query) {
    query.setStatus(QueryStatus.COMPLETED);
    query.setEndTime(new Date());
    updateAndNotify(query);
  }

  @Override
  public void fail(Query query) {
    query.setStatus(QueryStatus.FAILED);
    query.setEndTime(new Date());
    updateAndNotify(query);
  }

  @Override
  public void cancel(Query query) {
    query.setStatus(QueryStatus.CANCELLED);
    query.setEndTime(new Date());
    updateAndNotify(query);
  }

  private void updateAndNotify(Query query) {
    queryDao.update(query);
    QueryWebsocket.broadcast(query);
  }


  @Scheduled(fixedDelay = 5000)
  private void refreshPendingQueries() {
    List<Query> newQueries = queryDao.getNewQueries();
    if (!CollectionUtils.isEmpty(newQueries)) {
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

  @Scheduled(fixedDelay = 1000)
  private void run() {
    synchronized (pendingQueries) {
      while (!pendingQueries.isEmpty()) {
        executeQuery(pendingQueries.poll());
      }
    }
  }

  @Async
  private void executeQuery(Query query) {
    start(query);
    try {
      ArrayList<QueryResultSet> result = queryExecutorDao.execute(query);
      query.setResult(result);
      complete(query);
      notifyCallback(query);
    } catch (Exception e) {
      QueryError error = new QueryError();
      error.setMessage(ExceptionUtils.getMessage(e));
      error.setContent(ExceptionUtils.getStackTrace(e));
      query.setError(error);
      fail(query);
    }
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
        builder.post(Entity.json("Foobar"));
        break;
      case "GET":
        builder.get();
        break;
    }
//    Response response = builder.post(E);
  }

}