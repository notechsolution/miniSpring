package com.minis.beans.factory.support;

import com.minis.beans.BeansException;
import com.minis.beans.factory.BeanFactory;
import com.minis.beans.factory.ConfigurableBeanFactory;
import com.minis.beans.factory.FactoryBean;
import com.minis.beans.factory.config.AutowiredAnnotationBeanPostProcessor;
import com.minis.beans.factory.config.BeanDefinition;
import com.minis.beans.factory.config.BeanPostProcessor;
import com.minis.beans.factory.config.ConstructorArgumentValue;
import com.minis.beans.factory.config.ConstructorArgumentValues;
import com.minis.beans.factory.config.PropertyValue;
import com.minis.beans.factory.config.PropertyValues;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractBeanFactory extends DefaultSingletonBeanRegistry implements ConfigurableBeanFactory, BeanDefinitionRegistry {

  protected Map<String, BeanDefinition> beanDefinitions = new ConcurrentHashMap<>();
  private final List<BeanPostProcessor> beanPostProcessorList = new ArrayList<>();

  public AbstractBeanFactory() {
  }

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
            //step 1 postProcessBeforeInitialization
            applyBeanPostProcessorBeforeInitialization(singleton, beanName);
            //step 2 init method
            invokeInitMethod(beanDefinition, singleton);
            //step 3 postProcessAfterInitialization
            applyBeanPostProcessorAfterInitialization(singleton, beanName);
          } catch (Exception e) {
            e.printStackTrace();
            throw new BeansException(e.getMessage());
          }
        }
      }
    }
    if(singleton instanceof FactoryBean) {
      return this.getObjectForBeanInstance(singleton, beanName);
    }
    return singleton;
  }

  private Object getObjectForBeanInstance(Object singleton, String beanName) {
    if(!(singleton instanceof FactoryBean)) {
      return singleton;
    }
    FactoryBean<?> factoryBean = (FactoryBean<?>) singleton;
    Object object = getObjectFromFactoryBean(factoryBean, beanName);
    return object;
  }

  private Object getObjectFromFactoryBean(FactoryBean<?> factoryBean, String beanName) {
    Object object = null;
    try {
      object = factoryBean.getObject();
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
    return object;
  }

  private void invokeInitMethod(BeanDefinition beanDefinition, Object singleton) {
    if (beanDefinition.getInitMethodName() != null && !beanDefinition.getInitMethodName().equals("")) {
      Class<? extends Object> clazz = singleton.getClass();
      try {
        Method method = clazz.getMethod(beanDefinition.getInitMethodName());
        method.invoke(singleton);
      } catch (NoSuchMethodException e) {
        throw new RuntimeException(e);
      } catch (InvocationTargetException e) {
        throw new RuntimeException(e);
      } catch (IllegalAccessException e) {
        throw new RuntimeException(e);
      }
    }
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
    if (propertyValues == null || propertyValues.isEmpty()) {
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
            paramValue =  Integer.valueOf((String) propertyValue.getValue());
            break;
          }
          case "int": {
            paramType = int.class;
            paramValue =  Integer.parseInt((String) propertyValue.getValue());
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
    ConstructorArgumentValues constructorArgumentValues = beanDefinition.getConstructorArgumentValues();
    if (constructorArgumentValues != null && !constructorArgumentValues.isEmpty()) {
      Class<?>[] paramTypes = new Class<?>[constructorArgumentValues.getArgumentCount()];
      Object[] paramValues = new Object[constructorArgumentValues.getArgumentCount()];
      for (int i = 0; i < constructorArgumentValues.getArgumentCount(); i++) {
        ConstructorArgumentValue constructorArgumentValue = constructorArgumentValues.getIndexedArgumentValue(i);
        switch (constructorArgumentValue.getType()) {
          case "Integer": {
            paramTypes[i] = Integer.class;
            paramValues[i] = Integer.valueOf((String) constructorArgumentValue.getValue());
          }
          case "int": {
            paramTypes[i] = int.class;
            paramValues[i] = Integer.parseInt((String) constructorArgumentValue.getValue());
            break;
          }
          case "Boolean":
          case "bool": {
            paramTypes[i] = Boolean.class;
            paramValues[i] = Boolean.getBoolean((String) constructorArgumentValue.getValue());
            break;
          }
          default: {
            paramTypes[i] = String.class;
            paramValues[i] = constructorArgumentValue.getValue();
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

  public void refresh() {
    System.out.println("Refresh all beans now!");
    for (String beanName : beanDefinitions.keySet()) {
      try {
        getBean(beanName);
      } catch (BeansException e) {
        throw new RuntimeException(e);
      }
    }
  }

  abstract public Object applyBeanPostProcessorBeforeInitialization(Object existingBean, String beanName) throws BeansException;

  abstract public Object applyBeanPostProcessorAfterInitialization(Object existingBean, String beanName) throws BeansException;

  @Override
  public void registerBeanDefinition(String name, BeanDefinition beanDefinition) {
    this.beanDefinitions.put(name, beanDefinition);
  }

  @Override
  public void removeBeanDefinition(String name) {
    this.beanDefinitions.remove(name);

  }

  @Override
  public BeanDefinition getBeanDefinition(String name) {
    return this.beanDefinitions.get(name);
  }

  @Override
  public boolean containsBeanDefinition(String name) {
    return this.beanDefinitions.containsKey(name);
  }


  public List<BeanPostProcessor> getBeanPostProcessorList() {
    return beanPostProcessorList;
  }

  public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
    this.beanPostProcessorList.remove(beanPostProcessor);
    this.beanPostProcessorList.add(beanPostProcessor);
  }

  @Override
  public int getBeanPostProcessorCount() {
    return this.beanPostProcessorList.size();
  }
}
