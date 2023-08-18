package com.minis.aop.autoproxy;

import com.minis.aop.AopProxyFactory;
import com.minis.aop.DefaultAopProxyFactory;
import com.minis.aop.PointcutAdvisor;
import com.minis.aop.ProxyFactoryBean;
import com.minis.beans.BeansException;
import com.minis.beans.factory.BeanFactory;
import com.minis.beans.factory.config.BeanPostProcessor;
import com.minis.util.PatternMatchUtils;

public class BeanNameAutoProxyCreator implements BeanPostProcessor {

  private BeanFactory beanFactory;
  String pattern;
  private AopProxyFactory aopProxyFactory;
  private String interceptorName;
  private PointcutAdvisor advisor;

  public BeanNameAutoProxyCreator() {
    aopProxyFactory = new DefaultAopProxyFactory();
  }

  @Override
  public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
    if(isMatch(beanName, this.pattern)){
      System.out.println("[AOP]Inject proxy for beanName "+beanName);
      ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
      proxyFactoryBean.setBeanFactory(beanFactory);
      proxyFactoryBean.setTarget(bean);
      proxyFactoryBean.setInterceptorName(this.interceptorName);
      return proxyFactoryBean;
    }
    return bean;
  }

  private boolean isMatch(String beanName, String pattern) {
    return PatternMatchUtils.simpleMatch(pattern, beanName);
  }

  @Override
  public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
    return bean;
  }

  @Override
  public void setBeanFactory(BeanFactory beanFactory) {
    this.beanFactory = beanFactory;
  }

  public void setPattern(String pattern) {
    this.pattern = pattern;
  }

  public void setAopProxyFactory(AopProxyFactory aopProxyFactory) {
    this.aopProxyFactory = aopProxyFactory;
  }

  public void setInterceptorName(String interceptorName) {
    this.interceptorName = interceptorName;
  }

  public void setAdvisor(PointcutAdvisor advisor) {
    this.advisor = advisor;
  }
}
