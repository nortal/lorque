package com.nortal.lorque.query;

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
        + "ID NUMBER(19,0), "
        + "STATUS VARCHAR2(30 BYTE), "
        + "QUERY_SQL NVARCHAR2(1024), "
        + "QUERY_PARAMETERS NVARCHAR2(1024), "
        + "TYPE_CODE NVARCHAR2(50), "
        + "SUBMIT_TIME DATE, "
        + "START_TIME DATE, "
        + "END_TIME DATE, "
        + "CONSTRAINT PK_QUERY PRIMARY KEY (ID)" +
        ")";
    jdbcTemplate.execute(sql);
  }

  public List<Query> getActiveQueries() {
    String sql = "select * from query where sysdate between coalesce(start_time, submit_time) and coalesce(end_time, sysdate)";
    return jdbcTemplate.query(sql, new RowMapper<Query>() {
      @Override
      public Query mapRow(ResultSet rs, int rowNum) throws SQLException {
        Query query = new Query();
        query.setId(rs.getLong("id"));
        query.setStatus(rs.getString("status"));
        query.setQuerySql(rs.getString("query_sql"));
        query.setQueryParameters(rs.getString("query_parameters"));
        query.setSubmitTime(rs.getTimestamp("submit_time"));
        query.setStartTime(rs.getTimestamp("start_time"));
        query.setStartTime(rs.getTimestamp("end_time"));
        return query;
      }
    });
  }


}
