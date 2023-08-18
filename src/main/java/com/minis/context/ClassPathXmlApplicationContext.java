package com.minis.context;

import com.minis.beans.BeansException;
import com.minis.beans.factory.ConfigurableListableBeanFactory;
import com.minis.beans.factory.config.AutowiredAnnotationBeanPostProcessor;
import com.minis.beans.factory.config.BeanDefinition;
import com.minis.beans.factory.config.BeanPostProcessor;
import com.minis.beans.factory.support.AbstractAutowireCapableBeanFactory;
import com.minis.beans.factory.support.AbstractBeanFactory;
import com.minis.beans.factory.support.DefaultListableBeanFactory;
import com.minis.core.ClassPathXmlResource;
import com.minis.beans.factory.xml.XmlBeanDefinitionReader;

public class ClassPathXmlApplicationContext extends AbstractApplicationContext {

  private DefaultListableBeanFactory beanFactory;

  public ClassPathXmlApplicationContext(String fileName) {
    this(fileName, true);
  }
  public ClassPathXmlApplicationContext(String fileName, boolean isRefresh) {
    System.out.println("[IoC] ClassPathXmlApplicationContext try to start with configFile "+fileName);
    ClassPathXmlResource resource = new ClassPathXmlResource(fileName);
    beanFactory = new DefaultListableBeanFactory();
    XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);
    beanDefinitionReader.loadBeanDefinitions(resource);
    if(isRefresh){
      refresh();
    }
  }


  public Object getBean(String beanId) throws BeansException {
    return beanFactory.getBean(beanId);
  }

  @Override
  public ConfigurableListableBeanFactory getBeanFactory() {
    return beanFactory;
  }


  @Override
  public void registerListeners() {
  ApplicationListener listener = new ApplicationListener();
  this.getApplicationEventPublisher().addApplicationListener(listener);
  }

  @Override
  public void initApplicationEventPublisher() {
    ApplicationEventPublisher publisher = new SimpleApplicationEventPublisher();
    this.setApplicationEventPublisher(publisher);

  }

  @Override
  public void postProcessBeanFactory(ConfigurableListableBeanFactory bf) {

  }

  @Override
  public void registerBeanPostProcessors(ConfigurableListableBeanFactory bf) {
    bf.addBeanPostProcessor(new AutowiredAnnotationBeanPostProcessor());
    String[] beanDefinitionNames = this.beanFactory.getBeanDefinitionNames();
    for (String definitionName : beanDefinitionNames) {
      BeanDefinition beanDefinition = this.beanFactory.getBeanDefinition(definitionName);
      if(BeanPostProcessor.class.isAssignableFrom(beanDefinition.getBeanClass())) {
        System.out.println("[IoC] register BeanPostProcessor "+ definitionName);
        try {
          BeanPostProcessor beanPostProcessor = (BeanPostProcessor) this.beanFactory.getBean(definitionName);
          bf.addBeanPostProcessor(beanPostProcessor);
        } catch (BeansException e) {
          throw new RuntimeException(e);
        }
      }
    }


  }

  public void onRefresh() {
    this.beanFactory.refresh();
  }

  @Override
  public void finishRefresh() {
    publishEvent(new ContextRefreshEvent(this));
  }


  @Override
  public void publishEvent(ApplicationEvent event) {
    this.getApplicationEventPublisher().publishEvent(event);
  }
}
