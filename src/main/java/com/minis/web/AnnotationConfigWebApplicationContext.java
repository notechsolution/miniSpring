package com.minis.web;

import com.minis.context.ClassPathXmlApplicationContext;
import jakarta.servlet.ServletContext;

public class AnnotationConfigWebApplicationContext extends ClassPathXmlApplicationContext implements WebApplicationContext {

  private ServletContext servletContext;
  public AnnotationConfigWebApplicationContext(String contextLocation) {
    super(contextLocation);
  }

  @Override
  public ServletContext getServletContext() {
    return this.servletContext;
  }

  @Override
  public void setServletContext(ServletContext servletContext) {
  this.servletContext = servletContext;
  }
}
