package com.minis.web.servlet;

import com.minis.beans.BeansException;
import com.minis.web.annotation.RequestMapping;
import com.minis.web.WebApplicationContext;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class RequestMappingHandlerMapping implements HandlerMapping {

  private final MappingRegistry mappingRegistry = new MappingRegistry();
  WebApplicationContext webApplicationContext;

  public RequestMappingHandlerMapping(WebApplicationContext webApplicationContext) {
    this.webApplicationContext = webApplicationContext;
    initMapping();
  }

  private void initMapping() {
    Object controller;
    Class<?> clazz;
    for (String controllerName : webApplicationContext.getBeanDefinitionNames()) {
      try {
        controller = webApplicationContext.getBean(controllerName);
        clazz = Class.forName(controllerName);
      } catch (BeansException e) {
        throw new RuntimeException(e);
      } catch (ClassNotFoundException e) {
        throw new RuntimeException(e);
      }
      Method[] methods = clazz.getDeclaredMethods();
      if(methods!=null) {
        for (Method method : methods) {

          if(method.isAnnotationPresent(RequestMapping.class)){
            String path = method.getAnnotation(RequestMapping.class).value();
            mappingRegistry.registerHandlerMethod(path, controller, method);
          }
        }

      }
    }

  }

  @Override
  public HandlerMethod getHandler(HttpServletRequest request)  {
    return mappingRegistry.getHandler(request.getServletPath());
  }

  class MappingRegistry {

    Map <String, HandlerMethod> nameLookup = new HashMap<>();


    public Map<String, HandlerMethod> getNameLookup() {
      return nameLookup;
    }

    public void registerHandlerMethod(String path, Object handler, Method method) {
      HandlerMethod handlerMethod = new HandlerMethod(method, handler);
      this.nameLookup.put(path, handlerMethod);
    }

    public HandlerMethod getHandler(String path) {
      return this.nameLookup.get(path);
    }

  }
}
