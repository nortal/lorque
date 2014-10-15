package com.nortal.lorque.query;

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
  public void refreshPendingQueries() {
    List<Query> newQueries = queryDao.getNewQueries();
    if (!CollectionUtils.isEmpty(newQueries)) {
      synchronized (pendingQueries) {
        pendingQueries.clear();
        pendingQueries.addAll(newQueries);
      }
    }
    System.out.println(pendingQueries.size());
  }

}
