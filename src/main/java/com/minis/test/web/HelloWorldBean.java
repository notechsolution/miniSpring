package com.minis.test.web;

import com.minis.beans.factory.config.Autowired;
import com.minis.test.AService;
import com.minis.test.AServiceImpl;
import com.minis.web.RequestMapping;
import jakarta.servlet.http.HttpServletRequest;

public class HelloWorldBean {

  @Autowired
  AServiceImpl serviceA;
  @RequestMapping("/helloworld")
  public String getHelloWorld(HttpServletRequest request) {

    return "Hello " + serviceA.sayHello() +"!";
  }

  @RequestMapping("/bingo")
  public String bingo() {
    return "Hello bingo!";
  }

}
