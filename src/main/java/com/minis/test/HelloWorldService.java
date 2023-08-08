package com.minis.test;

import com.minis.beans.factory.config.Autowired;
import com.minis.jdbc.core.JdbcTemplate;
import com.minis.jdbc.core.RowMapper;
import com.minis.test.web.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class HelloWorldService {

  @Autowired
  JdbcTemplate jdbcTemplate;

  public HelloWorldService() {
  }

  public User getUser(Integer userId) {
    String sql = "select * from user where id="+userId;
    return (User) jdbcTemplate.query(sql, (statement) -> {
      ResultSet result = statement.executeQuery(sql);
      User user = new User();
      if(result.next()){
        user.setId(userId);
        user.setAge(result.getInt("age"));
        user.setName(result.getString("name"));
        user.setBirthday(result.getString("birthday"));
      }
      return user;
    });

  }

  public User getUserByName(String name) {
    String sql = "select * from user where name=?";
    return (User) jdbcTemplate.query(sql, new Object[]{name},(statement) -> {
      ResultSet result = statement.executeQuery();
      User user = new User();
      if(result.next()){
        user.setId(result.getInt("id"));
        user.setAge(result.getInt("age"));
        user.setName(result.getString("name"));
        user.setBirthday(result.getString("birthday"));
      }
      return user;
    });

  }

  public List<User> getAllUsers() {
    String sql = "select * from user";
    return (List<User>) jdbcTemplate.query(sql, null, new RowMapper<User>() {
      @Override
      public User mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        User user = new User();
        user.setId(resultSet.getInt("id"));
        user.setAge(resultSet.getInt("age"));
        user.setName(resultSet.getString("name"));
        user.setBirthday(resultSet.getString("birthday"));
        return user;
      }
    });

  }

}
