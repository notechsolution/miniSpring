package com.minis.beans.factory.support;

import com.minis.beans.BeansException;
import com.minis.beans.factory.BeanFactory;

public interface AutowireCapableBeanFactory extends BeanFactory {

  Object applyBeanPostProcessorBeforeInitialization(Object existingBean, String beanName) throws BeansException;

  Object applyBeanPostProcessorAfterInitialization(Object existingBean, String beanName) throws BeansException;
}
