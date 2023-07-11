package com.minis.test.web;

import jakarta.servlet.http.HttpServletRequest;
import java.net.http.HttpRequest;

public class HelloWorldBean {

  public String getHelloWorld(HttpServletRequest request) {
    return "Hello World!";
  }

}
