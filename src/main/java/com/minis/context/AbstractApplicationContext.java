package com.minis.context;

import com.minis.beans.BeansException;
import com.minis.beans.factory.ConfigurableListableBeanFactory;
import com.minis.beans.factory.config.BeanPostProcessor;
import java.util.Map;

public  abstract class AbstractApplicationContext implements ApplicationContext{

  private ApplicationEventPublisher applicationEventPublisher;

  @Override
  public Object getBean(String beanName) throws BeansException {
    return getBeanFactory().getBean(beanName);
  }

  @Override
  public void registerBean(String beanName, Object obj) {
    getBeanFactory().registerBean(beanName, obj);
  }

  @Override
  public boolean containsBean(String name) {
    return getBeanFactory().containsBean(name);
  }

  @Override
  public boolean isSingleton(String name) {
    return getBeanFactory().isSingleton(name);
  }

  @Override
  public boolean isPrototype(String name) {
    return getBeanFactory().isPrototype(name);
  }

  @Override
  public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
    getBeanFactory().addBeanPostProcessor(beanPostProcessor);
  }

  @Override
  public int getBeanPostProcessorCount() {
    return getBeanFactory().getBeanPostProcessorCount();
  }

  @Override
  public void registerDependentBean(String beanName, String dependentBeanName) {
    getBeanFactory().registerDependentBean(beanName, dependentBeanName);
  }

  @Override
  public String[] getDependentBeans(String beanName) {
    return getBeanFactory().getDependentBeans(beanName);
  }

  @Override
  public String[] getDependenciesForBean(String beanName) {
    return getBeanFactory().getDependenciesForBean(beanName);
  }

  @Override
  public boolean containsBeanDefinition(String beanName) {
    return getBeanFactory().containsBeanDefinition(beanName);
  }

  @Override
  public int getBeanDefinitionCount() {
    return getBeanFactory().getBeanDefinitionCount();
  }

  @Override
  public String[] getBeanDefinitionNames() {
    return getBeanFactory().getBeanDefinitionNames();
  }

  @Override
  public String[] getBeanNamesForType(Class<?> type) {
    return getBeanFactory().getBeanNamesForType(type);
  }

  @Override
  public <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException {
    return getBeanFactory().getBeansOfType(type);
  }

  @Override
  public void registerSingleton(String beanName, Object singletonObject) {
    getBeanFactory().registerSingleton(beanName,singletonObject);
  }

  @Override
  public Object getSingleton(String beanName) {
    return getBeanFactory().getSingleton(beanName);
  }

  @Override
  public boolean containsSingleton(String beanName) {
    return getBeanFactory().containsSingleton(beanName);
  }

  @Override
  public String[] getSingletonNames() {
    return getBeanFactory().getSingletonNames();
  }

  @Override
  public String getApplicationName() {
    return "NOT_DEFINED";
  }

  @Override
  public long getStartupDate() {
    return 0;
  }

  public abstract ConfigurableListableBeanFactory getBeanFactory();

  @Override
  public void refresh() {
    postProcessBeanFactory(getBeanFactory());

    registerBeanPostProcessors(getBeanFactory());

    initApplicationEventPublisher();

    onRefresh();

    registerListeners();

    finishRefresh();
  }

  public abstract void registerListeners();
  public abstract void initApplicationEventPublisher();
  public abstract void postProcessBeanFactory(ConfigurableListableBeanFactory bf);
  public abstract void registerBeanPostProcessors(ConfigurableListableBeanFactory bf);
  public abstract void onRefresh();
  public abstract void finishRefresh();

  @Override
  public void close() {

  }

  @Override
  public boolean isActive() {
    return true;
  }

  @Override
  public void publishEvent(ApplicationEvent event) {

  }

  @Override
  public void addApplicationListener(ApplicationListener listener) {

  }

  public ApplicationEventPublisher getApplicationEventPublisher() {
    return applicationEventPublisher;
  }

  public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
    this.applicationEventPublisher = applicationEventPublisher;
  }
}
