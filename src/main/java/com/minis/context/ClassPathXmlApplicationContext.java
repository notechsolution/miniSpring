package com.minis.context;

import com.minis.beans.BeansException;
import com.minis.beans.factory.config.AutowiredAnnotationBeanPostProcessor;
import com.minis.beans.factory.support.AbstractBeanFactory;
import com.minis.beans.factory.support.AutowireCapableBeanFactory;
import com.minis.core.ClassPathXmlResource;
import com.minis.beans.factory.support.SimpleBeanFactory;
import com.minis.beans.factory.xml.XmlBeanDefinitionReader;

public class ClassPathXmlApplicationContext implements ApplicationEventPublisher{

  private AbstractBeanFactory beanFactory;

  public ClassPathXmlApplicationContext(String fileName) {
    this(fileName, false);
  }
  public ClassPathXmlApplicationContext(String fileName, boolean isRefresh) {
    ClassPathXmlResource resource = new ClassPathXmlResource(fileName);
    beanFactory = new AutowireCapableBeanFactory();
    XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);
    beanDefinitionReader.loadBeanDefinitions(resource);
    if(isRefresh){
      refresh();
    }
  }


  public Object getBean(String beanId) throws BeansException {
    return beanFactory.getBean(beanId);
  }


  public void refresh(){
    registerBeanPostProcessors(this.beanFactory);
    onRefresh();
  }

  private void onRefresh() {
    this.beanFactory.refresh();
  }

  private void registerBeanPostProcessors(AbstractBeanFactory beanFactory) {
    beanFactory.addBeanPostProcessor(new AutowiredAnnotationBeanPostProcessor());
  }

  @Override
  public void publishEvent(ApplicationEvent event) {

  }
}
