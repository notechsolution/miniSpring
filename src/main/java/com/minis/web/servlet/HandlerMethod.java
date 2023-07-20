package com.minis.web.servlet;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class HandlerMethod {

  private Method method;
  private Parameter[] methodParameters;
  private Object bean;
  private Class<?> beanType;
  private Class<?> returnType;
  private String description;
  private String className;
  private String methodName;

  public HandlerMethod(Method method, Object bean) {
    this.method = method;
    this.bean = bean;
  }

  public Method getMethod() {
    return method;
  }

  public void setMethod(Method method) {
    this.method = method;
  }

  public Parameter[] getMethodParameters() {
    return methodParameters;
  }

  public void setMethodParameters(Parameter[] methodParameters) {
    this.methodParameters = methodParameters;
  }

  public Object getBean() {
    return bean;
  }

  public void setBean(Object bean) {
    this.bean = bean;
  }

  public Class<?> getBeanType() {
    return beanType;
  }

  public void setBeanType(Class<?> beanType) {
    this.beanType = beanType;
  }

  public Class<?> getReturnType() {
    return returnType;
  }

  public void setReturnType(Class<?> returnType) {
    this.returnType = returnType;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getClassName() {
    return className;
  }

  public void setClassName(String className) {
    this.className = className;
  }

  public String getMethodName() {
    return methodName;
  }

  public void setMethodName(String methodName) {
    this.methodName = methodName;
  }
}
