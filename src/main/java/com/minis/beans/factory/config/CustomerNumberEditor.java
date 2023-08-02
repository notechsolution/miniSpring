package com.minis.beans.factory.config;

import com.minis.util.NumberUtils;
import com.minis.util.StringUtils;
import java.text.NumberFormat;

public class CustomerNumberEditor implements PropertyEditor {

  private Class<? extends Number> numberClass;
  private NumberFormat numberFormat;
  private boolean allowEmpty;
  private Object value;

  public CustomerNumberEditor(Class<? extends Number> numberClass, boolean allowEmpty) {
    this(numberClass, null, allowEmpty);
  }

  public CustomerNumberEditor(Class<? extends Number> numberClass, NumberFormat numberFormat, boolean allowEmpty) {
    this.numberClass = numberClass;
    this.numberFormat = numberFormat;
    this.allowEmpty = allowEmpty;
  }

  @Override
  public void setAsText(String text) {
    if(allowEmpty && StringUtils.hasText(text)) {
      setValue(null);
    } else if (this.numberFormat !=null ) {
      setValue(NumberUtils.parseNumber(text, numberClass, numberFormat));
    } else {
      setValue(NumberUtils.parseNumber(text, numberClass));
    }

  }

  @Override
  public void setValue(Object value) {
    if (value instanceof Number) {
      this.value = NumberUtils.convertNumberToTargetClass((Number) value, this.numberClass);
    } else {
      this.value = value;
    }
  }

  @Override
  public Object getValue() {
    return value;
  }

  @Override
  public String getAsText() {
    if (value == null) {
      return "";
    }
    if (this.numberFormat != null) {
      return this.numberFormat.format(value);
    }
    return value.toString();

  }
}
