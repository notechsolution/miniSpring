package com.minis.aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class JdkDynamicAopProxy implements AopProxy, InvocationHandler {

  Object target;

  public JdkDynamicAopProxy(Object target) {
    this.target = target;
  }

  @Override
  public Object getProxy() {
    Object object = Proxy.newProxyInstance(JdkDynamicAopProxy.class.getClassLoader(), target.getClass().getInterfaces(), this);
    return object;
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    System.out.println("[AOP] do something before target method " + method.getName());
    return method.invoke(target, args);
  }
}
