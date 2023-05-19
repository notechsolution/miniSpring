package com.minis.beans;

public class PropertyValue {

  private final String name;
  private final String type;
  private final Object value;
  private final boolean isRef;

  public PropertyValue(String type, String name,  Object value, boolean isRef) {
    this.name = name;
    this.type = type;
    this.value = value;
    this.isRef = isRef;
  }

  public String getName() {
    return name;
  }

  public String getType() {
    return type;
  }

  public Object getValue() {
    return value;
  }

  public boolean isRef() {
    return isRef;
  }
}
