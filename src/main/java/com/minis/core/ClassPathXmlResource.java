package com.minis.core;

import java.net.URL;
import java.util.Iterator;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class ClassPathXmlResource implements Resource{
  Document document;
  Element rootElement;
  Iterator<Element> elementIterator;

  public ClassPathXmlResource(String fileName){
    SAXReader saxReader = new SAXReader();
    URL xmlPath = this.getClass().getClassLoader().getResource(fileName);
    try {
      document = saxReader.read(xmlPath);
      rootElement = document.getRootElement();
      elementIterator = rootElement.elementIterator();
    } catch (DocumentException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public boolean hasNext() {
    return elementIterator.hasNext();
  }

  @Override
  public Object next() {
    return elementIterator.next();
  }

  @Override
  public void remove() {
    elementIterator.remove();
  }
}
