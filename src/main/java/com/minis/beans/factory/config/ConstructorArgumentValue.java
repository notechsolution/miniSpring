package com.minis.beans.factory.config;

public class ConstructorArgumentValue {

  private final String name;
  private final String type;
  private final Object value;

  public ConstructorArgumentValue(String type, String name, Object value) {
    this.name = name;
    this.type = type;
    this.value = value;
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
}
