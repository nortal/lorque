package com.nortal.lorque.query;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
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
        + "END_TIME DATE"
        + ")";
    jdbcTemplate.execute(sql);
  }

  public List<Query> getActiveQueries() {
    String sql = "select * from query where status <> ?";
    return jdbcTemplate.query(sql, new QueryRowMapper(), QueryStatus.CANCELLED.name());
  }

  public Query getQuery(Long id) {
    try {
      return jdbcTemplate.queryForObject("select * from query where id = ?", new QueryRowMapper(), id);
    } catch (IncorrectResultSizeDataAccessException e) {
      return null;
    }
  }

  public void create(Query query) {
    String sql = "insert into query (status, query_sql, query_parameters, submit_time) values (?,?,?,?)";
    jdbcTemplate.update(sql, query.getStatus().name(), query.getQuerySql(), query.getQueryParameters(), query.getSubmitTime());
  }

  public void cancel(Long id) {
    jdbcTemplate.update("update query set status = ? where id = ?", QueryStatus.CANCELLED.name(), id);
  }

  public List<Query> getNewQueries() {
    return jdbcTemplate.query("select * from query where status = ?", new QueryRowMapper(), QueryStatus.SUBMITTED.name());
  }

  private class QueryRowMapper implements RowMapper<Query> {
    @Override
    public Query mapRow(ResultSet rs, int rowNum) throws SQLException {
      Query query = new Query();
      query.setId(rs.getLong("id"));
      query.setStatus(QueryStatus.valueOf(rs.getString("status")));
      query.setQuerySql(rs.getString("query_sql"));
      query.setQueryParameters(rs.getString("query_parameters"));
      query.setSubmitTime(rs.getTimestamp("submit_time"));
      query.setStartTime(rs.getTimestamp("start_time"));
      query.setStartTime(rs.getTimestamp("end_time"));
      return query;
    }
  }
}
