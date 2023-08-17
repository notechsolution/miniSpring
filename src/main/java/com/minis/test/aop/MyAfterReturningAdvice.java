package com.minis.test.aop;

import com.minis.aop.AfterReturningAdvice;
import java.lang.reflect.Method;

public class MyAfterReturningAdvice implements AfterReturningAdvice {

  @Override
  public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
    System.out.println("[AOP] test after returning interceptor for method "+method.getName());
  }
}
