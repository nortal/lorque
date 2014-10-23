package com.nortal.lorque.query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

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
    if (query == null) {
      return;
    }
    log.debug("Starting query id='" + query.getId() + "' execution.");
    queryDao.updateStatus(query.getId(), QueryStatus.SUBMITTED, QueryStatus.RUNNING);
    queryDao.updateStartTime(query.getId(), new Date());
    try {
      String result = queryExecutorDao.execute(query);
      queryDao.updateStatus(query.getId(), QueryStatus.RUNNING, QueryStatus.COMPLETED);
      log.debug("Query " + query.getId() + " result: " + result);
      queryDao.complete(query.getId(), result, new Date());
    } catch (SQLException | DataAccessException e) {
      queryDao.updateStatus(query.getId(), QueryStatus.RUNNING, QueryStatus.FAILED);
      // TODO: stack trace to string
      queryDao.complete(query.getId(), e.getMessage(), new Date());
    }
  }

}
