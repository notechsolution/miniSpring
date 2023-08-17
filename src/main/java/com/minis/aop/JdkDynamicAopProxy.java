package com.minis.aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class JdkDynamicAopProxy implements AopProxy, InvocationHandler {

  Object target;
  Advisor advisor;

  public JdkDynamicAopProxy(Object target, Advisor advisor) {
    this.target = target;
    this.advisor = advisor;
  }

  @Override
  public Object getProxy() {
    Object object = Proxy.newProxyInstance(JdkDynamicAopProxy.class.getClassLoader(), target.getClass().getInterfaces(), this);
    return object;
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    MethodInterceptor interceptor = advisor.getMethodInterceptor();
    MethodInvocation invocation = new ReflectiveMethodInvocation(proxy, target, method, args, target.getClass());
    return interceptor.invoke(invocation);
  }
}
