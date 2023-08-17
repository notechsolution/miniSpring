package com.minis.aop;

public class NameMatchMethodPointcutAdvisor implements PointcutAdvisor{

  private Advice advice;
  private String mappedName;
  private final NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
  private MethodInterceptor methodInterceptor;
  @Override
  public MethodInterceptor getMethodInterceptor() {
    return methodInterceptor;
  }

  @Override
  public void setMethodInterceptor(MethodInterceptor interceptor) {
    this.methodInterceptor = interceptor;

  }

  @Override
  public Pointcut getPointcut() {
    return pointcut;
  }


  public void setAdvice(Advice advice) {
    this.advice = advice;
    MethodInterceptor methodInterceptor = null;
    if(advice instanceof BeforeAdvice) {
      methodInterceptor = new MethodBeforeAdviceInterceptor((MethodBeforeAdvice) advice);
    } else if (advice instanceof AfterAdvice) {
      methodInterceptor = new AfterReturningAdviceInterceptor((AfterReturningAdvice) advice);
    } else if (advice instanceof MethodInterceptor) {
      methodInterceptor = (MethodInterceptor) advice;
    }
    setMethodInterceptor(methodInterceptor);
  }

  public void setMappedName(String mappedName) {
    this.mappedName = mappedName;
    pointcut.setMappedName(mappedName);
  }
}
