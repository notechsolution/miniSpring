package com.minis.jdbc.core;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

public class ArgumentPreparedStatementSetter {

  private final Object[] arguments;

  public ArgumentPreparedStatementSetter(Object[] arguments) {
    this.arguments = arguments;
  }

  public void setValues(PreparedStatement preparedStatement) throws SQLException {
    if(this.arguments!=null) {
      for (int i = 0; i < arguments.length; i++) {
        doSetValue(preparedStatement, i+1, arguments[i]);
      }
    }
  }

  private void doSetValue(PreparedStatement preparedStatement, int argumentIndex, Object argument) throws SQLException {
    if(argument instanceof String){
      preparedStatement.setString(argumentIndex, (String) argument);
    } else if (argument instanceof Integer) {
      preparedStatement.setInt(argumentIndex, (Integer) argument);
    } else if (argument instanceof java.util.Date) {
      preparedStatement.setDate(argumentIndex, new java.sql.Date(((Date) argument).getTime()));
    }
  }

}
