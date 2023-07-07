package com.minis.beans.factory.xml;

import com.minis.beans.factory.config.BeanDefinition;
import com.minis.beans.factory.config.ConstructorArgumentValue;
import com.minis.beans.factory.config.ConstructorArgumentValues;
import com.minis.beans.factory.config.PropertyValues;
import com.minis.beans.factory.support.AbstractBeanFactory;
import com.minis.core.Resource;
import java.util.ArrayList;
import java.util.List;
import org.dom4j.Element;

public class XmlBeanDefinitionReader {

  AbstractBeanFactory beanFactory;

  public XmlBeanDefinitionReader(AbstractBeanFactory beanFactory) {
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
      ConstructorArgumentValues constructorArgumentValues = new ConstructorArgumentValues();
      for (Element constructorElement : constructorElements) {
        String aType = constructorElement.attributeValue("type");
        String aName = constructorElement.attributeValue("name");
        String aValue = constructorElement.attributeValue("value");
        constructorArgumentValues.addArgumentValue(new ConstructorArgumentValue(aType, aName, aValue));
      }
      beanDefinition.setConstructorArgumentValues(constructorArgumentValues);

      beanFactory.registerBeanDefinition(beanDefinition.getId(), beanDefinition);
    }
  }

}
