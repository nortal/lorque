package com.nortal.lorque.query;

import com.nortal.lorque.Query;
import com.nortal.lorque.QueryStatus;

import java.util.List;

/**
 * @author Vassili Jakovlev
 */
public interface QueryService {

  List<Query> getActiveQueries();

  Query getQuery(Long id);

  void create(Query query);

  void start(Query query);

  void complete(Query query);

  void fail(Query query);

  void cancel(Query query);

  void setWebsocketCallback(WebsocketCallback callback);
}
