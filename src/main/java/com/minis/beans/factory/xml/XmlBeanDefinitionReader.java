package com.minis.beans.factory.xml;

import com.minis.BeanDefinition;
import com.minis.beans.ArgumentValue;
import com.minis.beans.ArgumentValues;
import com.minis.beans.PropertyValues;
import com.minis.beans.factory.SimpleBeanFactory;
import com.minis.core.Resource;
import java.util.ArrayList;
import java.util.List;
import org.dom4j.Element;

public class XmlBeanDefinitionReader {

  SimpleBeanFactory beanFactory;

  public XmlBeanDefinitionReader(SimpleBeanFactory beanFactory) {
    this.beanFactory = beanFactory;
  }

  public void loadBeanDefinitions(Resource resource) {
    while(resource.hasNext()) {
      Element element = (Element) resource.next();
      String beanId = element.attributeValue("id");
      String beanClassName = element.attributeValue("class");
      BeanDefinition beanDefinition = new BeanDefinition(beanId, beanClassName);

      List<String> refs = new ArrayList<>();
      List<Element> propertyElements = element.elements("property");
      PropertyValues propertyValues = new PropertyValues();
      for (Element propertyElement : propertyElements) {
        String pType = propertyElement.attributeValue("type");
        String pName = propertyElement.attributeValue("name");
        String pValue = propertyElement.attributeValue("value");
        String pRef = propertyElement.attributeValue("ref");
        boolean isRef = false;
        String value = "";
        if(pValue!=null && !pValue.equals("")) {
          value = pValue;
        } else if(pRef!=null && !pRef.equals("")) {
          value = pRef;
          isRef = true;
          refs.add(pRef);
        }
        propertyValues.addPropertyValue(pType,pName, value, isRef);
      }
      beanDefinition.setPropertyValues(propertyValues);
      beanDefinition.setDependsOn(refs.toArray(new String[0]));

      List<Element> constructorElements = element.elements("constructor-arg");
      ArgumentValues argumentValues = new ArgumentValues();
      for (Element constructorElement : constructorElements) {
        String aType = constructorElement.attributeValue("type");
        String aName = constructorElement.attributeValue("name");
        String aValue = constructorElement.attributeValue("value");
        argumentValues.addArgumentValue(new ArgumentValue(aType, aName, aValue));
      }
      beanDefinition.setConstructorArgumentValues(argumentValues);

      beanFactory.registerBeanDefinition(beanDefinition);
    }
  }

}
