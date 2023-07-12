package com.minis.test.web;

import com.minis.web.RequestMapping;
import jakarta.servlet.http.HttpServletRequest;

public class HelloWorldBean {

  @RequestMapping("/helloworld")
  public String getHelloWorld(HttpServletRequest request) {

    return "Hello " + request.getParameter("name") +"!";
  }

  @RequestMapping("/bingo")
  public String bingo() {
    return "Hello bingo!";
  }

}
