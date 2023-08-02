package com.minis.web;

import com.minis.beans.factory.config.BeanDefinition;
import com.minis.beans.factory.config.PropertyValues;
import com.minis.util.WebUtils;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;

public class WebDataBinder {

  private Object target;
  private Class<?> clz;
  private String objectName;
  private BeanWrapperImpl propertyAccessor;

  public WebDataBinder(Object target, String targetName) {
    this.target = target;
    this.objectName = targetName;
    this.clz = target.getClass();
    this.propertyAccessor = new BeanWrapperImpl(this.target);
  }

  public WebDataBinder(Object target) {
    this(target, "");
  }

  public void bind(HttpServletRequest request) {
    PropertyValues paramsPropertyValues = assignParameters(request);
    addBindValues(paramsPropertyValues, request);
    doBind(paramsPropertyValues);
  }

  private void doBind(PropertyValues paramsPropertyValues) {
    applyPropertyValues(paramsPropertyValues);
  }

  private void applyPropertyValues(PropertyValues paramsPropertyValues) {
    getPropertyAccessor().setPropertyValues(paramsPropertyValues);
  }

  private BeanWrapperImpl getPropertyAccessor() {
    return this.propertyAccessor;
  }

  // For primitive object???
  private void addBindValues(PropertyValues paramsPropertyValues, HttpServletRequest request) {

  }

  private PropertyValues assignParameters(HttpServletRequest request) {
    Map<String, Object> params = WebUtils.getParametersStartingWith(request, "");
    return new PropertyValues(params);
  }
}
