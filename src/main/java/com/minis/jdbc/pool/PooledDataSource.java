package com.minis.jdbc.pool;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import javax.sql.DataSource;

public class PooledDataSource implements DataSource {

  private String driverClassName;
  private String url;
  private String username;
  private String password;
  private Properties connectionProperties;
  private List<PooledConnection> connections;
  private int initialSize = 2;

  public PooledDataSource() {
  }

  private void initPool() throws SQLException {
    this.connections = new ArrayList<>();
    Properties mergedProps = new Properties();
    if(getConnectionProperties()!=null){
      mergedProps.putAll(getConnectionProperties());
    }
    if(username!=null){
      mergedProps.put("user", username);
    }
    if(password!=null){
      mergedProps.put("password", password);
    }

    for (int i = 0; i < initialSize; i++) {
      Connection connection = getConnectionFromDriverManager(getUrl(), mergedProps);
      PooledConnection pooledConnection = new PooledConnection(connection, false, String.valueOf(i+1));
      connections.add(pooledConnection);
      System.out.println("Initial Connection " + (i+1)+" Completed");
    }
  }

  @Override
  public Connection getConnection() throws SQLException {
    return getConnection(username, password);
  }

  private PooledConnection getAvailableConnection() {
    for (PooledConnection connection : this.connections) {
      if(!connection.isActive()){
        connection.setActive(true);
        System.out.println("Connection #"+ connection.getId()+" returned");
        return connection;
      }
    }

    return null;
  }

  @Override
  public Connection getConnection(String username, String password) throws SQLException {
    return getConnectionFromDriver(username, password);
  }

  private Connection getConnectionFromDriver(String username, String password) throws SQLException {
    if(this.connections == null){
      this.initPool();
    }

    PooledConnection connection = getAvailableConnection();
    while(connection == null){
      connection = getAvailableConnection();
      if(connection == null) {
        try {
          System.out.println("Connection pool is busy. wait for 30 seconds to retry");
          TimeUnit.MICROSECONDS.sleep(30);
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }
      }
    }
    return connection;
  }

  private Connection getConnectionFromDriverManager(String url, Properties mergedProps) throws SQLException {
    return DriverManager.getConnection(url, mergedProps);
  }

  @Override
  public PrintWriter getLogWriter() throws SQLException {
    return null;
  }

  @Override
  public void setLogWriter(PrintWriter out) throws SQLException {

  }

  @Override
  public void setLoginTimeout(int seconds) throws SQLException {

  }

  @Override
  public int getLoginTimeout() throws SQLException {
    return 0;
  }

  @Override
  public Logger getParentLogger() throws SQLFeatureNotSupportedException {
    return null;
  }

  @Override
  public <T> T unwrap(Class<T> iface) throws SQLException {
    return null;
  }

  @Override
  public boolean isWrapperFor(Class<?> iface) throws SQLException {
    return false;
  }

  public String getDriverClassName() {
    return driverClassName;
  }

  public void setDriverClassName(String driverClassName) {
    this.driverClassName = driverClassName;
    try {
      Class.forName(driverClassName);
    } catch (ClassNotFoundException e) {
      throw new IllegalStateException("Could not load JDBC driver class[" + driverClassName + "]");
    }
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Properties getConnectionProperties() {
    return connectionProperties;
  }

  public void setConnectionProperties(Properties connectionProperties) {
    this.connectionProperties = connectionProperties;
  }

  public int getInitialSize() {
    return initialSize;
  }

  public void setInitialSize(int initialSize) {
    this.initialSize = initialSize;
  }
}
