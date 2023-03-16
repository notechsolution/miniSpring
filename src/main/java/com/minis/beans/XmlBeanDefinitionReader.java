package com.minis.beans;

import com.minis.BeanDefinition;
import com.minis.core.Resource;
import org.dom4j.Element;

public class XmlBeanDefinitionReader {

  BeanFactory beanFactory;

  public XmlBeanDefinitionReader(BeanFactory beanFactory) {
    this.beanFactory = beanFactory;
  }

  public void loadBeanDefinitions(Resource resource) {
    while(resource.hasNext()) {
      Element element = (Element) resource.next();
      String beanId = element.attributeValue("id");
      String beanClassName = element.attributeValue("class");
      BeanDefinition beanDefinition = new BeanDefinition(beanId, beanClassName);
      beanFactory.registerBeanDefinition(beanDefinition);
    }
  }

}
