package com.minis.web;

import com.minis.core.Resource;
import java.util.HashMap;
import java.util.Map;
import org.dom4j.Element;

public class XmlMVCConfigReader {

  public Map<String, MapppingValue> loadConfig(Resource resource) {
    Map<String, MapppingValue> mappings = new HashMap<>();
    while(resource.hasNext()) {
      Element element = (Element) resource.next();
      String beanId = element.attributeValue("id");
      String beanClassName = element.attributeValue("class");
      String beanMethod = element.attributeValue("value");
      MapppingValue mapppingValue = new MapppingValue(beanId, beanClassName, beanMethod);
      mappings.put(beanId, mapppingValue);
    }
    return mappings;
  }

}
