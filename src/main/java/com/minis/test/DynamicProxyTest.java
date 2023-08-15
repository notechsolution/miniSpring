package com.minis.test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class DynamicProxyTest {

  public static void main(String[] args) {

    DynamicProxyTest dynamicProxyTest1 = new DynamicProxyTest();
    Hello hello = (Hello) dynamicProxyTest1.getProxy(new HelloImpl());
    hello.morning("miniSpring");

  }

  public Object getProxy(Object subject) {
    return Proxy.newProxyInstance(Hello.class.getClassLoader(), subject.getClass().getInterfaces(), new InvocationHandler() {
      @Override
      public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("do something before morning method " + method.getName());
        method.invoke(subject, args);
        return null;
      }
    });
  }
//  public Object getProxy(Object subject) {
//    return  Proxy.newProxyInstance(Hello.class.getClassLoader(), new Class[]{Hello.class}, new InvocationHandler() {
//      @Override
//      public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//        if(method.getName().equals("morning")){
//          System.out.println("do something before morning method");
//          method.invoke(subject, args);
//        }
//        return null;
//      }
//    });
//  }

  interface Hello {

    void morning(String name);
  }

  static class HelloImpl implements Hello {

    @Override
    public void morning(String name) {
      System.out.println("Good Morning:" + name);
    }
  }
}
