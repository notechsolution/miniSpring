package com.minis.web;

import com.minis.beans.factory.ConfigurableListableBeanFactory;
import com.minis.beans.factory.config.AutowiredAnnotationBeanPostProcessor;
import com.minis.beans.factory.config.BeanDefinition;
import com.minis.beans.factory.support.DefaultListableBeanFactory;
import com.minis.context.AbstractApplicationContext;
import com.minis.context.ApplicationEventPublisher;
import com.minis.context.ApplicationListener;
import com.minis.context.SimpleApplicationEventPublisher;
import com.minis.core.ClassPathXmlResource;
import com.minis.core.Resource;
import jakarta.servlet.ServletContext;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AnnotationConfigWebApplicationContext extends AbstractApplicationContext implements WebApplicationContext {

  private final WebApplicationContext parentWebApplicationContext;

  private DefaultListableBeanFactory beanFactory;
  private ServletContext servletContext;
  public AnnotationConfigWebApplicationContext(String contextLocation) {
    this(contextLocation, null);
  }

  public AnnotationConfigWebApplicationContext(String contextConfigLocation, WebApplicationContext parentWebApplicationContext) {
    this.parentWebApplicationContext = parentWebApplicationContext;
    this.servletContext = this.parentWebApplicationContext.getServletContext();
    URL xmlPath = null;
    try {
      xmlPath = getServletContext().getResource(contextConfigLocation);
    } catch (MalformedURLException e) {
      throw new RuntimeException(e);
    }
    Resource resource = new ClassPathXmlResource(xmlPath);
    beanFactory = new DefaultListableBeanFactory();
    beanFactory.setParentBeanFactory(parentWebApplicationContext.getBeanFactory());

    List<String> packageNames = XmlScanComponentHelper.getNodeValue(resource);
    loadBeanDefinition(packageNames);
    refresh();
  }

  private void loadBeanDefinition(List<String> packageNames) {
    List<String> controllerNames = scanPackages(packageNames);
    // load bean definition
    for (String controllerName : controllerNames) {
      String beanId = controllerName;
      String beanClassName = controllerName;
      BeanDefinition beanDefinition = new BeanDefinition(beanId, beanClassName);
      this.beanFactory.registerBeanDefinition(beanId, beanDefinition);
    }

  }

  private List<String> scanPackages(List<String> packageNames) {
    List<String> packages = new ArrayList<>();
    for (String packageName : packageNames) {
      packages.addAll(scanPackage(packageName));
    }

    return packages;
  }

  private List<String> scanPackage(String packageName) {
    List<String> controllerNames = new ArrayList<>();
    try {
      URI uri = this.getClass().getResource("/" + packageName.replaceAll("\\.", "/")).toURI();
      File dir = new File(uri);
      for (File file : dir.listFiles()) {
        if (file.isDirectory()) {
          controllerNames.addAll(scanPackage(packageName + "." + file.getName()));
        } else {
          if (file.getName().endsWith(".class")) {
            String controllerName = packageName + "." + file.getName().replace(".class", "");
            controllerNames.add(controllerName);
          } else {
            System.out.println("[miniMVC]Found invalid class file in base package " + file.getName());
          }
        }
      }

    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
    return controllerNames;
  }

  @Override
  public ServletContext getServletContext() {
    return this.servletContext;
  }

  @Override
  public void setServletContext(ServletContext servletContext) {
  this.servletContext = servletContext;
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
    this.beanFactory.addBeanPostProcessor(new AutowiredAnnotationBeanPostProcessor());

  }

  @Override
  public void onRefresh() {
    this.beanFactory.refresh();
  }

  @Override
  public void finishRefresh() {

  }
}
