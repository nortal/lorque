package com.nortal.lorque.query;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * @author Vassili Jakovlev
 */
@Service("queryService")
public class QueryServiceImpl implements QueryService {

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
    query.setSubmitTime(new Date());
    query.setStatus(QueryStatus.SUBMITTED);
    queryDao.create(query);
  }

  @Override
  public void cancel(Long id) {
    queryDao.cancel(id);
  }

  @Scheduled(fixedDelay = 5000)
  private void refreshPendingQueries() {
    List<Query> newQueries = queryDao.getNewQueries();
    if (!CollectionUtils.isEmpty(newQueries)) {
      System.out.println("Wooohooooo, new queries submitted!");
      synchronized (pendingQueries) {
        pendingQueries.clear();
        pendingQueries.addAll(newQueries);
      }
    }
    if (pendingQueries.isEmpty()) {
      System.out.println("No queries to process. Sleeping...");
    } else {
      System.out.println("Pending queries count: " + pendingQueries.size());
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
    if (query == null) {
      return;
    }
    System.out.println("Starting query id='" + query.getId() + "' execution.");
    updateStatus(query, QueryStatus.RUNNING);
    String result = queryExecutorDao.execute(query);
    System.out.println("Result: " + result);
  }


  private void updateStatus(Query query, QueryStatus toStatus) {
    QueryStatus fromStatus = query.getStatus();
    if (fromStatus == QueryStatus.CANCELLED) {
      throw new IllegalStateException("Cannot update status from " + fromStatus + " to " + toStatus);
    }
    queryDao.updateStatus(query.getId(), toStatus);
  }

}
