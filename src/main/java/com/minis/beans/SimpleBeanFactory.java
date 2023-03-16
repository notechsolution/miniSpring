package com.minis.beans;

import com.minis.BeanDefinition;
import java.util.HashMap;
import java.util.Map;

public class SimpleBeanFactory implements BeanFactory {

  private Map<String, BeanDefinition> beanDefinitions = new HashMap<>();
  private Map<String, Object> singletons = new HashMap<>();

  @Override
  public Object getBean(String beanName) throws BeansException {
    Object beanInstance = singletons.get(beanName);
    if(beanInstance == null) {
      BeanDefinition beanDefinition = beanDefinitions.get(beanName);
      if(beanDefinition==null){
        throw new BeansException("BeanName "+beanName+" can not find the definition");
      }else {
        try {
          beanInstance = Class.forName(beanDefinition.getClassName()).newInstance();
          singletons.put(beanName, beanInstance);
        } catch (Exception e) {
          throw new BeansException(e.getMessage());
        }
      }
    }
    return beanInstance;
  }

  @Override
  public void registerBeanDefinition(BeanDefinition beanDefinition) {
    beanDefinitions.put(beanDefinition.getId(), beanDefinition);
  }
}
