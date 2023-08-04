package com.minis.test.web;

import com.minis.beans.factory.config.Autowired;
import com.minis.jdbc.core.JdbcTemplate;
import java.sql.ResultSet;

public class HelloWorldService {

  @Autowired
  JdbcTemplate jdbcTemplate;

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

}
