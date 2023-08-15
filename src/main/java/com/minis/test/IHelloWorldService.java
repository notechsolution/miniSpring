package com.minis.test;

import com.minis.test.web.User;
import java.util.List;

public interface IHelloWorldService {

  User getUser(Integer userId);

  User getUserByName(String name);

  List<User> getAllUsers();
}
