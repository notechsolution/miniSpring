package com.minis.web;

import com.minis.beans.PropertyEditorRegistrySupport;
import com.minis.beans.factory.config.PropertyEditor;
import com.minis.beans.factory.config.PropertyValue;
import com.minis.beans.factory.config.PropertyValues;
import com.minis.util.StringUtils;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BeanWrapperImpl extends PropertyEditorRegistrySupport {

  private Object wrapperObject;
  Class<?> targetClass;
  PropertyValues requestParams;
  public BeanWrapperImpl(Object target) {
    this.wrapperObject = target;
    this.targetClass = target.getClass();
    registerDefaultEditors();
  }

  public void setPropertyValues(PropertyValues propertyValues) {
    this.requestParams = propertyValues;
    for (PropertyValue propertyValue : requestParams.getPropertyValueList()) {
      setPropertyValue(propertyValue);
    }

  }

  private void setPropertyValue(PropertyValue propertyValue) {
    BeanPropertyHandler propertyHandler = new BeanPropertyHandler(this.wrapperObject, propertyValue.getName());
    PropertyEditor fieldEditor = getDefaultEditor(propertyHandler.getPropertyClass());
    fieldEditor.setAsText((String) propertyValue.getValue());

    propertyHandler.setValue(fieldEditor.getValue());
  }

  private class BeanPropertyHandler {

    private final Method writeMethod;
    private final Method readMethod;
    private Object target;
    private Class<?> targetClass;
    private Class<?> propertyClass;

    public BeanPropertyHandler(Object wrapperObject, String fieldName) {
      this.target = wrapperObject;
      this.targetClass = wrapperObject.getClass();
      try {
        Field field = this.targetClass.getDeclaredField(fieldName);
        this.propertyClass = field.getType();
        this.writeMethod = this.targetClass.getMethod("set"+ StringUtils.capitalize(fieldName), field.getType());
        this.readMethod = this.targetClass.getMethod("get"+ StringUtils.capitalize(fieldName));
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
    public void setValue(Object value) {
      writeMethod.setAccessible(true);
      try {
        writeMethod.invoke(target, value);
      } catch (IllegalAccessException e) {
        throw new RuntimeException(e);
      } catch (InvocationTargetException e) {
        throw new RuntimeException(e);
      }
    }

    public Object getValue(){
      readMethod.setAccessible(true);
      try {
        return readMethod.invoke(target);
      } catch (IllegalAccessException e) {
        throw new RuntimeException(e);
      } catch (InvocationTargetException e) {
        throw new RuntimeException(e);
      }
    }

    public Class<?> getPropertyClass() {
      return propertyClass;
    }
  }
}
