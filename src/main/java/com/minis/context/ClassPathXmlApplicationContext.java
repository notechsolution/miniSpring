package com.minis.context;

import com.minis.beans.BeanFactory;
import com.minis.beans.BeansException;
import com.minis.core.ClassPathXmlResource;
import com.minis.beans.SimpleBeanFactory;
import com.minis.beans.XmlBeanDefinitionReader;

public class ClassPathXmlApplicationContext {

  private BeanFactory beanFactory;
  public ClassPathXmlApplicationContext(String fileName) {
    ClassPathXmlResource resource = new ClassPathXmlResource(fileName);
    beanFactory = new SimpleBeanFactory();
    XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);
    beanDefinitionReader.loadBeanDefinitions(resource);
  }


  public Object getBean(String beanId) throws BeansException {
    return beanFactory.getBean(beanId);
  }


}
