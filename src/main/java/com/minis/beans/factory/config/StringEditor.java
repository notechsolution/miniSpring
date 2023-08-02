package com.minis.beans.factory.config;

public class StringEditor implements PropertyEditor {

  private Class<String> stringClass;
  private String strFormat;
  private boolean allowEmpty;
  private Object value;

  public StringEditor(Class<String> stringClass, String strFormat, boolean allowEmpty) {
    this.stringClass = stringClass;
    this.strFormat = strFormat;
    this.allowEmpty = allowEmpty;
  }

  public StringEditor(Class<String> stringClass, boolean allowEmpty) {
    this(stringClass,"", allowEmpty);
  }

  @Override
  public void setAsText(String text) {
    setValue(text);
  }

  @Override
  public void setValue(Object value) {
    this.value = value;
  }

  @Override
  public Object getValue() {
    return this.value;
  }

  @Override
  public String getAsText() {
    return value.toString();
  }
}
