package com.minis.context;

import com.minis.beans.BeansException;
import com.minis.core.ClassPathXmlResource;
import com.minis.beans.factory.SimpleBeanFactory;
import com.minis.beans.factory.xml.XmlBeanDefinitionReader;

public class ClassPathXmlApplicationContext implements ApplicationEventPublisher{

  private SimpleBeanFactory beanFactory;

  public ClassPathXmlApplicationContext(String fileName) {
    this(fileName, false);
  }
  public ClassPathXmlApplicationContext(String fileName, boolean isRefresh) {
    ClassPathXmlResource resource = new ClassPathXmlResource(fileName);
    beanFactory = new SimpleBeanFactory();
    XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);
    beanDefinitionReader.loadBeanDefinitions(resource);
    if(isRefresh){
      beanFactory.refresh();
    }
  }


  public Object getBean(String beanId) throws BeansException {
    return beanFactory.getBean(beanId);
  }


  @Override
  public void publishEvent(ApplicationEvent event) {

  }
}
