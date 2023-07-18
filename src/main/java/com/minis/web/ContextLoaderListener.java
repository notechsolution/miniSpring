package com.minis.web;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

public class ContextLoaderListener implements ServletContextListener {

  private static final String CONFIG_LOCATION_PARAM = "contextConfigLocation";
  private WebApplicationContext webApplicationContext;

  public ContextLoaderListener() {
  }

  public ContextLoaderListener(WebApplicationContext context) {
    this.webApplicationContext = context;
  }

  @Override
  public void contextDestroyed(ServletContextEvent sce) {
    System.out.println("ContextLoaderListener context destroyed");
  }

  @Override
  public void contextInitialized(ServletContextEvent sce) {
    initWebApplicationContext(sce.getServletContext());
  }

  private void initWebApplicationContext(ServletContext servletContext) {
    String contextLocation = servletContext.getInitParameter(CONFIG_LOCATION_PARAM);
    WebApplicationContext context = new AnnotationConfigWebApplicationContext(contextLocation);
    context.setServletContext(servletContext);
    this.webApplicationContext = context;
    servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, this.webApplicationContext);
  }
}
