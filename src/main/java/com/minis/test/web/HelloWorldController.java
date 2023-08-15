package com.minis.test.web;

import com.minis.beans.factory.config.Autowired;
import com.minis.test.AServiceImpl;
import com.minis.test.IHelloWorldService;
import com.minis.web.annotation.RequestMapping;
import com.minis.web.annotation.RequestParam;
import com.minis.web.annotation.ResponseBody;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

public class HelloWorldController {

  @Autowired
  AServiceImpl serviceA;

  @Autowired
  IHelloWorldService helloWorldService;

  @RequestMapping("/helloworld")
  public String getHelloWorld(HttpServletRequest request) {

    return "Hello " + serviceA.sayHello() + "!";
  }

  @RequestMapping("/bingo")
  @ResponseBody
  public HelloDTO bingo(@RequestParam("name") String name, HelloDTO helloDTO) {
    helloDTO.setName("SERVER:" + helloDTO.getName());
    System.out.println("SERVER:" + helloDTO.getName());
    return helloDTO;
  }

  @RequestMapping("/user")
  @ResponseBody
  public User getUser(@RequestParam("id") Integer userId, @RequestParam("name")String name, HttpServletRequest request) {

    User user = null;
    if(userId!=null && !"".equals(userId)){
      user = helloWorldService.getUser(userId);
    } else if (name!=null && !"".equals(name)) {
      user = helloWorldService.getUserByName(name);
    }
    System.out.println("get user #" + userId + " with result " + user.toString());
    return user;
  }

  @RequestMapping("/user/all")
  @ResponseBody
  public List<User> getAllUser() {
    return this.helloWorldService.getAllUsers();
  }
}
