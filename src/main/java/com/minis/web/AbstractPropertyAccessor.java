package com.minis.web;

import com.minis.beans.PropertyEditorRegistrySupport;
import com.minis.beans.factory.config.PropertyValue;
import com.minis.beans.factory.config.PropertyValues;

public abstract class AbstractPropertyAccessor extends PropertyEditorRegistrySupport {

  PropertyValues requestParams;

  public void setPropertyValues(PropertyValues propertyValues) {
    this.requestParams = propertyValues;
    for (PropertyValue propertyValue : requestParams.getPropertyValueList()) {
      setPropertyValue(propertyValue);
    }

  }

  protected abstract void setPropertyValue(PropertyValue propertyValue);
}
