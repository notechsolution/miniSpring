package com.minis.beans.factory;

import com.minis.BeanDefinition;
import com.minis.beans.ArgumentValue;
import com.minis.beans.ArgumentValues;
import com.minis.beans.BeansException;
import com.minis.beans.PropertyValue;
import com.minis.beans.PropertyValues;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleBeanFactory extends DefaultSingletonBeanRegistry implements BeanFactory {

  private Map<String, BeanDefinition> beanDefinitions = new ConcurrentHashMap<>();

  @Override
  public Object getBean(String beanName) throws BeansException {
    Object singleton = getSingleton(beanName);
    if (singleton == null) {
      singleton = this.earlySingletonObjects.get(beanName);
      if (singleton == null) {
        BeanDefinition beanDefinition = beanDefinitions.get(beanName);
        if (beanDefinition == null) {
          throw new BeansException("BeanName " + beanName + " can not find the definition");
        } else {
          try {
            singleton = createBean(beanDefinition);
            registerSingleton(beanName, singleton);
            // bean post processor
          } catch (Exception e) {
            e.printStackTrace();
            throw new BeansException(e.getMessage());
          }
        }
      }

    }
    return singleton;
  }

  private Object createBean(BeanDefinition beanDefinition) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
    Class<?> clz = Class.forName(beanDefinition.getClassName());
    // handle constructor with arguments
    Object beanInstance = doCreateBean(beanDefinition, clz);
    this.earlySingletonObjects.put(beanDefinition.getId(), beanInstance);
    // handle properties
    handleProperties(beanDefinition, clz, beanInstance);

    return beanInstance;
  }

  private void handleProperties(BeanDefinition beanDefinition, Class<?> clz, Object beanInstance)
      throws IllegalAccessException, ClassNotFoundException {
    System.out.println("handle properties for bean: " + beanDefinition.getId());
    PropertyValues propertyValues = beanDefinition.getPropertyValues();
    if (propertyValues.isEmpty()) {
      System.out.println("no property defined for bean: " + beanDefinition.getId());
      return;
    }

    for (int i = 0; i < propertyValues.size(); i++) {
      PropertyValue propertyValue = propertyValues.getPropertyValueList().get(i);
      Class<?> paramType = null;
      Object paramValue;
      if (!propertyValue.isRef()) {
        paramValue = propertyValue.getValue();
        switch (propertyValue.getType()) {
          case "Integer": {
            paramType = Integer.class;
            break;
          }
          case "int": {
            paramType = int.class;
            break;
          }
          default:
            paramType = String.class;
        }
      } else {
        paramType = Class.forName(propertyValue.getType());
        try {
          paramValue = getBean((String) propertyValue.getValue());
        } catch (BeansException e) {
          throw new RuntimeException(e);
        }
      }

      String methodName = "set" + propertyValue.getName().substring(0, 1).toUpperCase() + propertyValue.getName().substring(1);
      try {
        Method method = clz.getMethod(methodName, paramType);
        method.invoke(beanInstance, paramValue);
      } catch (NoSuchMethodException | InvocationTargetException e) {
        throw new RuntimeException(e);
      }
    }
  }

  private static Object doCreateBean(BeanDefinition beanDefinition, Class<?> clz) throws InstantiationException, IllegalAccessException {
    Object beanInstance;
    ArgumentValues argumentValues = beanDefinition.getConstructorArgumentValues();
    if (!argumentValues.isEmpty()) {
      Class<?>[] paramTypes = new Class<?>[argumentValues.getArgumentCount()];
      Object[] paramValues = new Object[argumentValues.getArgumentCount()];
      for (int i = 0; i < argumentValues.getArgumentCount(); i++) {
        ArgumentValue argumentValue = argumentValues.getIndexedArgumentValue(i);
        switch (argumentValue.getType()) {
          case "Integer": {
            paramTypes[i] = Integer.class;
            paramValues[i] = Integer.valueOf((String) argumentValue.getValue());
          }
          case "int": {
            paramTypes[i] = int.class;
            paramValues[i] = Integer.parseInt((String) argumentValue.getValue());
            break;
          }
          case "Boolean":
          case "bool": {
            paramTypes[i] = Boolean.class;
            paramValues[i] = Boolean.getBoolean((String) argumentValue.getValue());
            break;
          }
          default: {
            paramTypes[i] = String.class;
            paramValues[i] = argumentValue.getValue();
          }
        }
      }
      try {
        beanInstance = clz.getConstructor(paramTypes).newInstance(paramValues);
      } catch (InvocationTargetException e) {
        throw new RuntimeException(e);
      } catch (NoSuchMethodException e) {
        throw new RuntimeException(e);
      }

    } else {
      beanInstance = clz.newInstance();
    }
    return beanInstance;
  }

  @Override
  public void registerBean(String beanName, Object obj) {
    registerSingleton(beanName, obj);
  }

  @Override
  public boolean containsBean(String name) {
    return containsSingleton(name);
  }

  @Override
  public boolean isSingleton(String name) {
    return beanDefinitions.get(name).isSingleton();
  }

  @Override
  public boolean isPrototype(String name) {
    return beanDefinitions.get(name).isPrototype();
  }

  public void registerBeanDefinition(BeanDefinition beanDefinition) {
    this.beanDefinitions.put(beanDefinition.getId(), beanDefinition);
  }

  public void refresh(){
    System.out.println("Refresh all beans now!");
    for (String beanName : beanDefinitions.keySet()) {
      try {
        getBean(beanName);
      } catch (BeansException e) {
        throw new RuntimeException(e);
      }
    }

  }

}
