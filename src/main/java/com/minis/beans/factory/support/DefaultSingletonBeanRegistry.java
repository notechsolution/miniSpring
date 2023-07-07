package com.minis.beans.factory.support;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {

  private Map<String, Object> singletons = new ConcurrentHashMap<>();
  protected Map<String, Object> earlySingletonObjects = new ConcurrentHashMap<>();

  @Override
  public void registerSingleton(String beanName, Object singletonObject) {
    synchronized (singletons) {
      singletons.put(beanName, singletonObject);
    }
  }

  @Override
  public Object getSingleton(String beanName) {
    return singletons.get(beanName);
  }

  @Override
  public boolean containsSingleton(String beanName) {
    return singletons.containsKey(beanName);
  }

  @Override
  public String[] getSingletonNames() {
    return singletons.keySet().toArray(new String[0]);
  }
}
