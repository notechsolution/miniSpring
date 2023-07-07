package com.minis.beans.factory.config;

import com.minis.beans.BeansException;
import com.minis.beans.factory.BeanFactory;
import com.minis.beans.factory.support.AbstractAutowireCapableBeanFactory;
import java.lang.reflect.Field;

public class AutowiredAnnotationBeanPostProcessor implements BeanPostProcessor {

  private BeanFactory beanFactory;

  @Override
  public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
    Object result = bean;

    Class<? extends Object> clazz = bean.getClass();
    Field[] fields = clazz.getDeclaredFields();
    if (fields != null) {
      for (Field field : fields) {
        boolean isAutowired = field.isAnnotationPresent(Autowired.class);
        if(isAutowired){
          System.out.println("field "+field.getName() +" isAutowired "+ isAutowired);
          String fieldName = field.getName();
          Object autowiredObject = this.getBeanFactory().getBean(fieldName);
          try {
            field.setAccessible(true);
            field.set(bean, autowiredObject);
            System.out.println("autowire "+ fieldName +" for bean "+ beanName);
          } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
          }
        }
      }

    }
    return result;
  }

  @Override
  public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
    return null;
  }

  public BeanFactory getBeanFactory() {
    return beanFactory;
  }

  public void setBeanFactory(BeanFactory beanFactory) {
    this.beanFactory = beanFactory;
  }
}
