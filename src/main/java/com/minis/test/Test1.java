package com.minis.test;

import com.minis.context.ClassPathXmlApplicationContextAllInOne;

public class Test1 {

  public static void main(String[] args) {
    ClassPathXmlApplicationContextAllInOne context = new ClassPathXmlApplicationContextAllInOne("beans.xml");
    AService aService = (AService) context.getBean("aservice");
    aService.sayHello();
  }
}
