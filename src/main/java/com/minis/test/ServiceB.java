package com.minis.test;

public class ServiceB {

  private ServiceC serviceC;

  public void sayHiToCaller() {
    System.out.println("Hey, I Am sayHiToCaller from ServiceB");
  }

  public ServiceC getServiceC() {
    return serviceC;
  }

  public void setServiceC(ServiceC serviceC) {
    this.serviceC = serviceC;
  }
}
