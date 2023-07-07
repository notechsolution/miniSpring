package com.minis.beans.factory.support;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {

  private Map<String, Object> singletons = new ConcurrentHashMap<>();
  protected Map<String, Object> earlySingletonObjects = new ConcurrentHashMap<>();

  protected Map<String, Set<String>> dependentBeanMap = new ConcurrentHashMap<>();
  protected Map<String, Set<String>> dependenciesForBeanMap = new ConcurrentHashMap<>();

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

  public void registerDependentBean(String beanName, String dependentBeanName) {
    Set<String> dependentBeans = this.dependentBeanMap.get(beanName);
    if (dependentBeans != null && dependentBeans.contains(dependentBeanName)) {
      return;
    }

    // No entry yet -> fully synchronized manipulation of the dependentBeans Set
    synchronized (this.dependentBeanMap) {
      dependentBeans = this.dependentBeanMap.get(beanName);
      if (dependentBeans == null) {
        dependentBeans = new LinkedHashSet<String>(8);
        this.dependentBeanMap.put(beanName, dependentBeans);
      }
      dependentBeans.add(dependentBeanName);
    }
    synchronized (this.dependenciesForBeanMap) {
      Set<String> dependenciesForBean = this.dependenciesForBeanMap.get(dependentBeanName);
      if (dependenciesForBean == null) {
        dependenciesForBean = new LinkedHashSet<String>(8);
        this.dependenciesForBeanMap.put(dependentBeanName, dependenciesForBean);
      }
      dependenciesForBean.add(beanName);
    }

  }

  public boolean hasDependentBean(String beanName) {
    return this.dependentBeanMap.containsKey(beanName);
  }

  public String[] getDependentBeans(String beanName) {
    Set<String> dependentBeans = this.dependentBeanMap.get(beanName);
    if (dependentBeans == null) {
      return new String[0];
    }
    return (String[]) dependentBeans.toArray();
  }

  public String[] getDependenciesForBean(String beanName) {
    Set<String> dependenciesForBean = this.dependenciesForBeanMap.get(beanName);
    if (dependenciesForBean == null) {
      return new String[0];
    }
    return (String[]) dependenciesForBean.toArray();

  }
}
