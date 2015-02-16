package com.nortal.lorque.query;

/**
 * @author Vassili Jakovlev
 */
//@Repository
public class QueryDao {

//  @Resource(name = "internalJdbcTemplate")
//  protected JdbcTemplate jdbcTemplate;
//  private Gson gson;
//
//  @PostConstruct
//  public void init() {
//    gson = GsonProvider.getGsonBuilder().create();
//    String sql = "CREATE TABLE QUERY ("
//        + "ID NUMBER(19,0) IDENTITY NOT NULL, "
//        + "STATUS VARCHAR2(30 BYTE) NOT NULL, "
//        + "QUERY_SQL NVARCHAR2(1024), "
//        + "QUERY_PARAMETERS NVARCHAR2(1024), "
//        + "CALLBACK_URL NVARCHAR2(1024), "
//        + "CALLBACK_METHOD NVARCHAR2(10), "
//        + "SUBMIT_TIME DATE NOT NULL, "
//        + "START_TIME DATE, "
//        + "END_TIME DATE,"
//        + "RESULT CLOB,"
//        + "ERROR CLOB"
//        + ")";
//    jdbcTemplate.execute(sql);
//  }
//
//  public List<Query> getActiveQueries() {
//    String sql = "select * from query where status <> ?";
//    return jdbcTemplate.query(sql, new QueryRowMapper(false), QueryStatus.CANCELLED.name());
//  }
//
//  public Query getQuery(Long id) {
//    try {
//      return jdbcTemplate.queryForObject("select * from query where id = ?", new QueryRowMapper(true), id);
//    } catch (IncorrectResultSizeDataAccessException e) {
//      return null;
//    }
//  }
//
//  public Long create(Query query) {
//    String sql = "insert into query (status, query_sql, query_parameters, callback_url, callback_method, submit_time) values (?,?,?,?,?,?)";
//    String callback = query.getCallback() == null ? null : query.getCallback().getUrl();
//    String callbackMethod = query.getCallback() == null ? null : query.getCallback().getMethod();
//    jdbcTemplate.update(sql, query.getStatus().name(), query.getSql(),
//        gson.toJson(query.getParameters()), callback, callbackMethod, query.getSubmitTime());
//    return jdbcTemplate.queryForObject("call identity()", Long.class);
//  }
//
//  public List<Query> getNewQueries() {
//    return jdbcTemplate.query("select * from query where status = ?", new QueryRowMapper(false), QueryStatus.SUBMITTED.name());
//  }
//
//  public int update(Query query) {
//    String result = gson.toJson(query.getResult());
//    String error = gson.toJson(query.getError());
//    return jdbcTemplate.update("update query set start_time = ?, end_time = ?, status = ?, result = ?, error = ? where id = ?", query.getStartTime(), query.getEndTime(),
//        query.getStatus().name(), result, error, query.getId());
//  }
//
//  private class QueryRowMapper implements RowMapper<Query> {
//    private final boolean includeResult;
//
//
//    private QueryRowMapper(boolean includeResult) {
//      this.includeResult = includeResult;
//    }
//
//    @Override
//    public Query mapRow(ResultSet rs, int rowNum) throws SQLException {
//      Query query = new Query();
//      query.setId(rs.getLong("id"));
//      query.setStatus(QueryStatus.valueOf(rs.getString("status")));
//      query.setSql(rs.getString("query_sql"));
//      String parameters = rs.getString("query_parameters");
//      query.setParameters(parameters == null ? null : gson.fromJson(parameters, new TypeToken<List<QueryParameter>>() {
//      }.getType()));
//      String callbackUrl = rs.getString("callback_url");
//      if (callbackUrl != null) {
//        ExecutionCallback callback = new ExecutionCallback();
//        callback.setUrl(callbackUrl);
//        callback.setMethod(rs.getString("callback_method"));
//        query.setCallback(callback);
//      }
//      query.setSubmitTime(rs.getTimestamp("submit_time"));
//      query.setStartTime(rs.getTimestamp("start_time"));
//      query.setEndTime(rs.getTimestamp("end_time"));
//      if (includeResult) {
//        String result = rs.getString("result");
//        if (result != null) {
//          query.setResult(gson.fromJson(result, new TypeToken<List<QueryResultSet>>() {
//          }.getType()));
//        }
//        query.setError(gson.fromJson(rs.getString("error"), QueryError.class));
//      }
//      return query;
//    }
//  }
}
