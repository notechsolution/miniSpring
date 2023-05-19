package com.minis.test;

public class ServiceC {

  private AServiceImpl serviceA;

  public void sayHiToCaller() {
    System.out.println("Hey, I Am sayHiToCaller from ServiceC");
  }

  public AServiceImpl getServiceA() {
    return serviceA;
  }

  public void setServiceA(AServiceImpl serviceA) {
    this.serviceA = serviceA;
  }
}
