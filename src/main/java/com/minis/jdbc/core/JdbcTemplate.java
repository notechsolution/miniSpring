package com.minis.jdbc.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
      if(arguments!=null && arguments.length>0){
        for(int i=0;i<arguments.length; i++){
          Object argument = arguments[i];
          if(argument instanceof String) {
            statement.setString(i+1, (String) argument);
          } else if(argument instanceof Integer){
            statement.setInt(i+1, (Integer) argument);
          }
        }
      }
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

  public DataSource getDataSource() {
    return dataSource;
  }

  public void setDataSource(DataSource dataSource) {
    this.dataSource = dataSource;
  }

}
