package com.nortal.lorque.query;

import java.util.List;

/**
 * @author Vassili Jakovlev
 */
public interface QueryService {

  List<Query> getActiveQueries();

  Query getQuery(Long id);

  void create(Query query);

  void cancel(Long id);
}
