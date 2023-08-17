package com.minis.aop;

public class AfterReturningAdviceInterceptor implements MethodInterceptor, AfterAdvice {

  private final AfterReturningAdvice advice;

  public AfterReturningAdviceInterceptor(AfterReturningAdvice advice) {
    this.advice = advice;
  }

  @Override
  public Object invoke(MethodInvocation invocation) throws Throwable {
    Object returningObject = invocation.proceed();
    advice.afterReturning(returningObject,invocation.getMethod(), invocation.getArguments(), invocation.getThis());
    return returningObject;
  }
}
