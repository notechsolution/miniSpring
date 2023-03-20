package com.minis.beans.factory;

import com.minis.BeanDefinition;
import com.minis.beans.BeansException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleBeanFactory extends DefaultSingletonBeanRegistry implements BeanFactory {

  private Map<String, BeanDefinition> beanDefinitions = new ConcurrentHashMap<>();

  @Override
  public Object getBean(String beanName) throws BeansException {
    Object beanInstance = getSingleton(beanName);
    if(beanInstance == null) {
      BeanDefinition beanDefinition = beanDefinitions.get(beanName);
      if(beanDefinition==null){
        throw new BeansException("BeanName "+beanName+" can not find the definition");
      }else {
        try {
          beanInstance = Class.forName(beanDefinition.getClassName()).newInstance();
          registerSingleton(beanName, beanInstance);
        } catch (Exception e) {
          throw new BeansException(e.getMessage());
        }
      }
    }
    return beanInstance;
  }

  @Override
  public void registerBean(String beanName, Object obj) {
    registerSingleton(beanName, obj);
  }

  @Override
  public boolean containsBean(String name) {
    return containsSingleton(name);
  }

  @Override
  public boolean isSingleton(String name) {
    return beanDefinitions.get(name).isSingleton();
  }

  @Override
  public boolean isPrototype(String name) {
    return beanDefinitions.get(name).isPrototype();
  }

  public void registerBeanDefinition(BeanDefinition beanDefinition){
    this.beanDefinitions.put(beanDefinition.getId(), beanDefinition);
  }

}
