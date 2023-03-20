package com.minis.beans;

public class ArgumentValue {

  private final String name;
  private final String type;
  private final Object value;

  public ArgumentValue(String name, String type, Object value) {
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
