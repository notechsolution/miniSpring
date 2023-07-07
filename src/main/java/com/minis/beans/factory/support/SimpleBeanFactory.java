package com.minis.beans.factory.support;

import com.minis.beans.BeansException;

public class SimpleBeanFactory extends AbstractBeanFactory{

  @Override
  public Object applyBeanPostProcessorBeforeInitialization(Object existingBean, String beanName) throws BeansException {
    return null;
  }

  @Override
  public Object applyBeanPostProcessorAfterInitialization(Object existingBean, String beanName) throws BeansException {
    return null;
  }
}
