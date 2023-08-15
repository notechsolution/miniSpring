package com.minis.aop;

import com.minis.beans.factory.FactoryBean;

public class ProxyFactoryBean implements FactoryBean<Object> {

  Object singletonInstance;
  Object target;
  private AopProxyFactory aopProxyFactory;

  public ProxyFactoryBean() {
    aopProxyFactory = new DefaultAopProxyFactory();
  }

  @Override
  public Object getObject() throws Exception {
    return getSingletonInstance();
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
      return singletonInstance = getProxy(createAopProxy(target));
    }
    return singletonInstance;
  }

  private Object getProxy(AopProxy aopProxy) {
    return aopProxy.getProxy();
  }

  private AopProxy createAopProxy(Object target) {
    return aopProxyFactory.createAopProxy(target);
  }


  public Object getTarget() {
    return target;
  }

  public void setTarget(Object target) {
    this.target = target;
  }
}
