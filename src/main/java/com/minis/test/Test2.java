package com.minis.test;

import com.minis.context.ClassPathXmlApplicationContext;
import com.minis.beans.BeansException;

public class Test2 {

  public static void main(String[] args) throws BeansException {
//    ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
    ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("beans.xml", true);
    System.out.println("start to get bean");
    AService aService = (AService) context.getBean("aservice");
    aService.sayHello();
  }
}
