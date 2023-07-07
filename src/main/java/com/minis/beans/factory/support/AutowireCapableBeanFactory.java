package com.minis.beans.factory.support;

import com.minis.beans.BeansException;
import com.minis.beans.factory.config.AutowiredAnnotationBeanPostProcessor;
import java.util.ArrayList;
import java.util.List;

public class AutowireCapableBeanFactory extends AbstractBeanFactory{

  @Override
  public Object applyBeanPostProcessorBeforeInitialization(Object existingBean, String beanName) throws BeansException {
//    System.out.println("start applyBeanPostProcessorBeforeInitialization...");
    Object result = existingBean;
    for (AutowiredAnnotationBeanPostProcessor beanPostProcessor : getBeanPostProcessorList()) {
      beanPostProcessor.setBeanFactory(this);
      result = beanPostProcessor.postProcessBeforeInitialization(result, beanName);
      if(result == null){
        return null;
      }
    }

    return result;
  }

  @Override
  public Object applyBeanPostProcessorAfterInitialization(Object existingBean, String beanName) throws BeansException {
//    System.out.println("start applyBeanPostProcessorAfterInitialization...");
    Object result = existingBean;
    for (AutowiredAnnotationBeanPostProcessor beanPostProcessor : getBeanPostProcessorList()) {
      beanPostProcessor.setBeanFactory(this);
      result = beanPostProcessor.postProcessAfterInitialization(result, beanName);
      if(result == null){
        return null;
      }
    }

    return result;
  }
}
