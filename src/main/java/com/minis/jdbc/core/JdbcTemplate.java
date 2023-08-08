package com.minis.jdbc.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;

public class JdbcTemplate {

  private DataSource dataSource;

  public Object query(String sql, StatementCallback statementCallback) {
    Connection connection = null;
    PreparedStatement statement = null;
    try{
      connection = dataSource.getConnection();
      statement = connection.prepareStatement(sql);
      return statementCallback.doInStatement(statement);
    }catch (Exception e){
      e.printStackTrace();
    }finally {
      try {
        statement.close();
        connection.close();
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }

    }
    return null;
  }

  public Object query(String sql, Object[] arguments, PreparedStatementCallback statementCallback){

    Connection connection = null;
    PreparedStatement statement = null;
    try{
      connection = dataSource.getConnection();
      statement = connection.prepareStatement(sql);
      ArgumentPreparedStatementSetter statementSetter = new ArgumentPreparedStatementSetter(arguments);
      statementSetter.setValues(statement);
      return statementCallback.doInPreparedStatement(statement);
    }catch (Exception e){
      e.printStackTrace();
    }finally {
      try {
        statement.close();
        connection.close();
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }

    }
    return null;
  }

  public <T> List<T> query(String sql, Object[] arguments,RowMapper<T> rowMapper){

    Connection connection = null;
    PreparedStatement statement = null;
    try{
      connection = dataSource.getConnection();
      statement = connection.prepareStatement(sql);
      ArgumentPreparedStatementSetter statementSetter = new ArgumentPreparedStatementSetter(arguments);
      statementSetter.setValues(statement);
      ResultSet result = statement.executeQuery();
      RowMapperResultSetExtractor<T> resultSetExtractor = new RowMapperResultSetExtractor<>(rowMapper);
      return resultSetExtractor.extractData(result);
    }catch (Exception e){
      e.printStackTrace();
    }finally {
      try {
        statement.close();
        connection.close();
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }

    }
    return null;
  }

  public DataSource getDataSource() {
    return dataSource;
  }

  public void setDataSource(DataSource dataSource) {
    this.dataSource = dataSource;
  }

}
