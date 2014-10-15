package com.nortal.lorque.query;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Vassili Jakovlev
 */
@Service("queryService")
public class QueryServiceImpl implements QueryService {

  @Resource
  private QueryDao queryDao;

  @Override
  public List<Query> getActiveQueries() {
    return queryDao.getActiveQueries();
  }
}
