package com.minis.aop;

import com.minis.beans.BeansException;
import com.minis.beans.factory.BeanFactory;
import com.minis.beans.factory.BeanFactoryAware;
import com.minis.beans.factory.FactoryBean;

public class ProxyFactoryBean implements FactoryBean<Object>, BeanFactoryAware {

  Object singletonInstance;
  Object target;
  private AopProxyFactory aopProxyFactory;

  private String interceptorName;
  private PointcutAdvisor advisor;

  private BeanFactory beanFactory;

  public ProxyFactoryBean() {
    aopProxyFactory = new DefaultAopProxyFactory();
  }

  @Override
  public Object getObject() throws Exception {
    initializeAdvisor();
    return getSingletonInstance();
  }

  private synchronized void initializeAdvisor() {
    Object advice = null;
    try {
      advice = this.beanFactory.getBean(this.interceptorName);
    } catch (BeansException e) {
      throw new RuntimeException(e);
    }

    advisor = (PointcutAdvisor) advice;
  }

  @Override
  public Class<?> getObjectType() {
    if (singletonInstance != null) {
      return singletonInstance.getClass();
    }
    return null;
  }

  private synchronized Object getSingletonInstance() {
    if (singletonInstance == null) {
      return singletonInstance = getProxy(createAopProxy(target, advisor));
    }
    return singletonInstance;
  }

  private Object getProxy(AopProxy aopProxy) {
    return aopProxy.getProxy();
  }

  private AopProxy createAopProxy(Object target, PointcutAdvisor advisor) {
    return aopProxyFactory.createAopProxy(target, advisor);
  }


  public Object getTarget() {
    return target;
  }

  public void setTarget(Object target) {
    this.target = target;
  }

  @Override
  public void setBeanFactory(BeanFactory beanFactory) {
    this.beanFactory = beanFactory;
  }


  public String getInterceptorName() {
    return interceptorName;
  }

  public void setInterceptorName(String interceptorName) {
    this.interceptorName = interceptorName;
  }
}
