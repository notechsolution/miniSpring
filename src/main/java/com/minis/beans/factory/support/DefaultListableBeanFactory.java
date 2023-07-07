package com.minis.beans.factory.support;

import com.minis.beans.BeansException;
import com.minis.beans.factory.ConfigurableListableBeanFactory;
import com.minis.beans.factory.config.BeanDefinition;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class DefaultListableBeanFactory extends AbstractAutowireCapableBeanFactory implements ConfigurableListableBeanFactory {

  @Override
  public int getBeanDefinitionCount() {
    return this.beanDefinitions.size();
  }

  @Override
  public String[] getBeanDefinitionNames() {
    return this.beanDefinitions.keySet().toArray(new String[0]);
  }

  @Override
  public String[] getBeanNamesForType(Class<?> type) {
    List<String> beanNames = new ArrayList<>();
    for (Entry<String, BeanDefinition> entry : this.beanDefinitions.entrySet()) {
      if(entry.getValue().getBeanClass().isAssignableFrom(type)) {
        beanNames.add(entry.getKey());
      }
    }

    return (String[]) beanNames.toArray();
  }

  @Override
  public <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException {
    Map<String, T> beans = new HashMap<>();
    for (Entry<String, BeanDefinition> entry : this.beanDefinitions.entrySet()) {
      if(entry.getValue().getBeanClass().isAssignableFrom(type)) {
        beans.put(entry.getKey(), (T) getBean(entry.getKey()));
      }
    }
    return beans;
  }
}
