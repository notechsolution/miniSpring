package com.minis.beans;

import com.minis.BeanDefinition;

public interface BeanDefinitionRegistry {

  void registerBeanDefinition(String name, BeanDefinition beanDefinition);
  void removeBeanDefinition(String name);
  BeanDefinition getBeanDefinition(String name);
  boolean containsBeanDefinition(String name);

}
