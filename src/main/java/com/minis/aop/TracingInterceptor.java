package com.minis.aop;

public class TracingInterceptor implements MethodInterceptor {

  @Override
  public Object invoke(MethodInvocation invocation) throws Throwable {
    System.out.println("[AOP] method "+invocation.getMethod().getName()+ " is called on " + invocation.getThis()+" with args "+ invocation.getArguments());
    Object result = invocation.proceed();
    System.out.println("[AOP] method "+invocation.getMethod().getName()+ " returns " + result);
    return result;
  }
}
