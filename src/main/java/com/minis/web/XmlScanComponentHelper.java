package com.minis.web;

import com.minis.core.Resource;
import java.util.ArrayList;
import java.util.List;
import org.dom4j.Element;

public class XmlScanComponentHelper {

  public static List<String> getNodeValue(Resource resource) {
    List<String> packages = new ArrayList<>();
    while(resource.hasNext()) {
      Element element = (Element) resource.next();
      String basePackage = element.attributeValue("base-package");
      packages.add(basePackage);
    }
    return packages;
  }
}
