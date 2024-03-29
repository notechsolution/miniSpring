package com.minis.test;

import com.minis.beans.factory.config.Autowired;

public class AServiceImpl implements AService {

  private String name;
  private int level;
  private String property1;
  private String property2;

  @Autowired
  private ServiceB serviceB;

  public AServiceImpl() {
  }

  public AServiceImpl(String name, int level) {
    this.name = name;
    this.level = level;
    System.out.println(name + "," + level);
  }

  @Override
  public String sayHello() {
    String hello = "[AService]" + name + " with level " + level + ": " + property1 + ":" + property2;
    System.out.println(hello);
    serviceB.sayHiToCaller();
    return hello;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getLevel() {
    return level;
  }

  public void setLevel(int level) {
    this.level = level;
  }

  public String getProperty1() {
    return property1;
  }

  public void setProperty1(String property1) {
    this.property1 = property1;
  }

  public String getProperty2() {
    return property2;
  }

  public void setProperty2(String property2) {
    this.property2 = property2;
  }

  public ServiceB getServiceB() {
    return serviceB;
  }

  public void setServiceB(ServiceB serviceB) {
    this.serviceB = serviceB;
  }
}
