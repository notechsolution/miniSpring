package com.minis.beans;

import com.minis.beans.factory.config.CustomerNumberEditor;
import com.minis.beans.factory.config.PropertyEditor;
import com.minis.beans.factory.config.StringEditor;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class PropertyEditorRegistrySupport {

  private Map<Class<?>, PropertyEditor> defaultEditors;
  private Map<Class<?>, PropertyEditor> customEditors = new HashMap<>();

  public void registerDefaultEditors() {
    createDefaultEditors();
  }

  private void createDefaultEditors() {
    this.defaultEditors = new HashMap<>();
    this.defaultEditors.put(int.class, new CustomerNumberEditor(Integer.class, false));
    this.defaultEditors.put(Integer.class, new CustomerNumberEditor(Integer.class, true));
    this.defaultEditors.put(long.class, new CustomerNumberEditor(Long.class, false));
    this.defaultEditors.put(Long.class, new CustomerNumberEditor(Long.class, true));
    this.defaultEditors.put(float.class, new CustomerNumberEditor(Float.class, false));
    this.defaultEditors.put(Float.class, new CustomerNumberEditor(Float.class, true));
    this.defaultEditors.put(double.class, new CustomerNumberEditor(Double.class, false));
    this.defaultEditors.put(Double.class, new CustomerNumberEditor(Double.class, true));
    this.defaultEditors.put(BigInteger.class, new CustomerNumberEditor(BigInteger.class, true));
    this.defaultEditors.put(BigDecimal.class, new CustomerNumberEditor(BigDecimal.class, true));
    this.defaultEditors.put(String.class, new StringEditor(String.class, true));
  }

  public void registerCustomEditor(Class<?> requiredType, PropertyEditor propertyEditor) {
    customEditors.put(requiredType, propertyEditor);
  }

  public PropertyEditor findCustomEditor(Class<?> requiredType) {
    return getCustomEditor(requiredType);
  }

  public boolean hasCustomEditorForElement(Class<?> elementType) {
    return this.customEditors.containsKey(elementType);
  }

  private PropertyEditor getCustomEditor(Class<?> requiredType) {
    if (requiredType == null || this.customEditors.isEmpty()) {
      return null;
    }

    return this.customEditors.get(requiredType);
  }

  public PropertyEditor getDefaultEditor(Class<?> requiredType) {

    return this.defaultEditors.get(requiredType);
  }

}
