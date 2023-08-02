package com.minis.beans.factory.config;

public interface PropertyEditor {

  void setAsText(String text);
  void setValue(Object value);
  Object getValue();
  String getAsText();
}
