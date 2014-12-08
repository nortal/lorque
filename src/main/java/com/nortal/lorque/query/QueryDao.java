package com.nortal.lorque.query;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author Vassili Jakovlev
 */
@Repository
public class QueryDao {

  @Resource(name = "internalJdbcTemplate")
  protected JdbcTemplate jdbcTemplate;


  @PostConstruct
  public void init() {
    String sql = "CREATE TABLE QUERY ("
        + "ID NUMBER(19,0) IDENTITY NOT NULL, "
        + "STATUS VARCHAR2(30 BYTE) NOT NULL, "
        + "QUERY_SQL NVARCHAR2(1024), "
        + "QUERY_PARAMETERS NVARCHAR2(1024), "
        + "SUBMIT_TIME DATE NOT NULL, "
        + "START_TIME DATE, "
        + "END_TIME DATE,"
        + "RESULT CLOB,"
        + "ERROR CLOB"
        + ")";
    jdbcTemplate.execute(sql);
  }

  public List<Query> getActiveQueries() {
    String sql = "select * from query where status <> ?";
    return jdbcTemplate.query(sql, new QueryRowMapper(false), QueryStatus.CANCELLED.name());
  }

  public Query getQuery(Long id) {
    try {
      return jdbcTemplate.queryForObject("select * from query where id = ?", new QueryRowMapper(true), id);
    } catch (IncorrectResultSizeDataAccessException e) {
      return null;
    }
  }

  public Long create(Query query) {
    String sql = "insert into query (status, query_sql, query_parameters, submit_time) values (?,?,?,?)";
    jdbcTemplate.update(sql, query.getStatus().name(), query.getQuerySql(), StringUtils.join(query.getQueryParameters(), ", "), query.getSubmitTime());
    return jdbcTemplate.queryForObject("call identity()", Long.class);
  }

  public void cancel(Long id) {
    jdbcTemplate.update("update query set status = ? where id = ?", QueryStatus.CANCELLED.name(), id);
  }

  public List<Query> getNewQueries() {
    return jdbcTemplate.query("select * from query where status = ?", new QueryRowMapper(false), QueryStatus.SUBMITTED.name());
  }

  public void updateStatus(Long queryId, QueryStatus from, QueryStatus to) {
    int rowsUpdated = jdbcTemplate.update("update query set status = ? where id = ? and status = ?", to.name(), queryId, from.name());
    if (rowsUpdated == 0) {
      throw new IllegalStateException("Failed to update status from " + from + " to " + to);
    }
  }

  public void updateStartTime(Long queryId, Date startTime) {
    jdbcTemplate.update("update query set start_time = ? where id = ? and start_time is null", startTime, queryId);
  }

  public void complete(Long queryId, String result, String error, Date endTime) {
    jdbcTemplate.update("update query set result = ?, error = ?, end_time = ? where id = ?", result, error, endTime, queryId);
  }

  private class QueryRowMapper implements RowMapper<Query> {
    private final boolean includeResult;

    private QueryRowMapper(boolean includeResult) {
      this.includeResult = includeResult;
    }

    @Override
    public Query mapRow(ResultSet rs, int rowNum) throws SQLException {
      Query query = new Query();
      query.setId(rs.getLong("id"));
      query.setStatus(QueryStatus.valueOf(rs.getString("status")));
      query.setQuerySql(rs.getString("query_sql"));
      String[] params = StringUtils.split(rs.getString("query_parameters"), ", ");
      query.setQueryParameters(params == null ? null : Arrays.asList(params));
      query.setSubmitTime(rs.getTimestamp("submit_time"));
      query.setStartTime(rs.getTimestamp("start_time"));
      query.setEndTime(rs.getTimestamp("end_time"));
      if (includeResult) {
        JsonParser parser = new JsonParser();
        String result = rs.getString("result");
        if (result != null) {
          query.setResult(parser.parse(result).getAsJsonArray());
        }
        query.setError(new Gson().fromJson(rs.getString("error"), QueryError.class));
      }
      return query;
    }
  }
}
