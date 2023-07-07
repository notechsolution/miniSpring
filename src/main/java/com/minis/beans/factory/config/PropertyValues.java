package com.minis.beans.factory.config;

import java.util.ArrayList;
import java.util.List;

public class PropertyValues {

  private final List<PropertyValue> propertyValueList = new ArrayList<>();

  public PropertyValues() {
  }

  public List<PropertyValue> getPropertyValueList() {
    return propertyValueList;
  }

  public int size() {
    return propertyValueList.size();
  }

  public void addPropertyValue(PropertyValue propertyValue) {
    propertyValueList.add(propertyValue);
  }

  public void addPropertyValue(String type, String name, Object value, boolean isRef) {
    addPropertyValue(new PropertyValue(type, name, value, isRef));
  }

  public void removePropertyValue(PropertyValue propertyValue) {
    propertyValueList.remove(propertyValue);
  }

  public void removePropertyValue(String propertyName) {
    propertyValueList.remove(getPropertyValue(propertyName));
  }

  public Object get(String propertyName) {
    PropertyValue propertyValue = getPropertyValue(propertyName);
    return propertyValue != null ? propertyValue.getValue() : null;
  }

  private PropertyValue getPropertyValue(String propertyName) {
    return propertyValueList.stream().filter(propertyValue -> propertyValue.getName().equals(propertyName)).findFirst().orElseGet(null);
  }

  public boolean contains(String propertyName) {
    return getPropertyValue(propertyName)!=null;
  }

  public boolean isEmpty() {
    return propertyValueList.isEmpty();
  }

}
