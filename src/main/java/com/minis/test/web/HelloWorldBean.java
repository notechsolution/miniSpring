package com.minis.test.web;

import com.minis.beans.factory.config.Autowired;
import com.minis.test.AServiceImpl;
import com.minis.web.annotation.RequestMapping;
import com.minis.web.annotation.RequestParam;
import com.minis.web.annotation.ResponseBody;
import jakarta.servlet.http.HttpServletRequest;

public class HelloWorldBean {

  @Autowired
  AServiceImpl serviceA;
  @RequestMapping("/helloworld")
  public String getHelloWorld(HttpServletRequest request) {

    return "Hello " + serviceA.sayHello() +"!";
  }

  @RequestMapping("/bingo")
  @ResponseBody
  public HelloDTO bingo(@RequestParam("name") String name, HelloDTO helloDTO) {
    helloDTO.setName("SERVER:"+helloDTO.getName());
    System.out.println("SERVER:"+helloDTO.getName());
    return helloDTO;
  }

}
