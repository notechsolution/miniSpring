package com.minis.context;

import com.minis.BeanDefinition;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class ClassPathXmlApplicationContextAllInOne {

  private List<BeanDefinition> beanDefinitions = new ArrayList<>();
  private Map<String, Object> singletons = new HashMap<>();

  public ClassPathXmlApplicationContextAllInOne(String fileName) {
    this.readXml(fileName);
    this.instanceBeans();
  }


  private void readXml(String fileName) {
    SAXReader saxReader = new SAXReader();
    URL xmlPath = this.getClass().getClassLoader().getResource(fileName);
    try {
      Document document = saxReader.read(xmlPath);
      Element rootElement = document.getRootElement();
      for (Element element : (List<Element>)rootElement.elements()) {
        String beanId = element.attributeValue("id");
        String beanClassName = element.attributeValue("class");
        BeanDefinition beanDefinition = new BeanDefinition(beanId, beanClassName);
        beanDefinitions.add(beanDefinition);
      }

    } catch (DocumentException e) {
      throw new RuntimeException(e);
    }
  }

  private void instanceBeans() {
    for (BeanDefinition beanDefinition : beanDefinitions) {
      try {
        Object newObject = Class.forName(beanDefinition.getClassName()).getConstructor().newInstance();
        singletons.put(beanDefinition.getId(), newObject);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }

  }

  public Object getBean(String beanId) {
    return singletons.get(beanId);
  }


}
