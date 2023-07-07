package com.minis.beans.factory.config;

public class BeanDefinition {

  String SCOPE_SINGLETON = "singleton";
  String SCOPE_PROTOTYPE = "prototype";
  private boolean lazyInit = false;
  private String[] dependsOn;
  private ConstructorArgumentValues constructorArgumentValues;
  private PropertyValues propertyValues;
  private String initMethodName;
  private String scope = SCOPE_SINGLETON;
  private String id;

  private String className;

  private Class<?> beanClass;

  public BeanDefinition(String id, String className){
    this.id = id;
    this.className = className;
    try {
      this.beanClass = Class.forName(className);
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getClassName() {
    return className;
  }

  public void setClassName(String className) {
    this.className = className;
  }

  public boolean isLazyInit() {
    return lazyInit;
  }

  public void setLazyInit(boolean lazyInit) {
    this.lazyInit = lazyInit;
  }

  public String[] getDependsOn() {
    return dependsOn;
  }

  public void setDependsOn(String[] dependsOn) {
    this.dependsOn = dependsOn;
  }

  public ConstructorArgumentValues getConstructorArgumentValues() {
    return constructorArgumentValues;
  }

  public void setConstructorArgumentValues(ConstructorArgumentValues constructorArgumentValues) {
    this.constructorArgumentValues = constructorArgumentValues;
  }

  public PropertyValues getPropertyValues() {
    return propertyValues;
  }

  public void setPropertyValues(PropertyValues propertyValues) {
    this.propertyValues = propertyValues;
  }

  public String getInitMethodName() {
    return initMethodName;
  }

  public void setInitMethodName(String initMethodName) {
    this.initMethodName = initMethodName;
  }

  public String getScope() {
    return scope;
  }

  public void setScope(String scope) {
    this.scope = scope;
  }

  public boolean isSingleton() {
    return SCOPE_SINGLETON.equals(scope);
  }

  public boolean isPrototype() {
    return SCOPE_PROTOTYPE.equals(scope);
  }

  public Class<?> getBeanClass() {
    return beanClass;
  }

  public void setBeanClass(Class<?> beanClass) {
    this.beanClass = beanClass;
  }
}
