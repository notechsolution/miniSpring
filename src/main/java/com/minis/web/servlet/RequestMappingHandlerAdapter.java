package com.minis.web.servlet;

import com.minis.web.WebApplicationContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

public class RequestMappingHandlerAdapter implements HandlerAdapter {

  WebApplicationContext webApplicationContext;

  public RequestMappingHandlerAdapter(WebApplicationContext webApplicationContext) {
    this.webApplicationContext = webApplicationContext;
  }

  @Override
  public void handler(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
    handleInternal(request, response, (HandlerMethod) handler);
  }

  private void handleInternal(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler) throws IOException {
    Object controller = handler.getBean();
    Object result = null;
    try {
      Method method = handler.getMethod();
      System.out.println(method.getParameters().length);
      List<Object> arguments = prepareArguments(method, request, response);
      if(arguments.size() == 0) {
        result = method.invoke(controller);
      } else {
        result = method.invoke(controller, arguments.toArray());
      }
    } catch (IllegalAccessException | InvocationTargetException e) {
      throw new RuntimeException(e);
    }
    if (result != null) {
      response.getWriter().append(result.toString());
    } else {

      response.getWriter().append("no result from controller");
    }
  }

  private List<Object> prepareArguments(Method method, HttpServletRequest req, HttpServletResponse resp) {
    List<Object> objects = new ArrayList<>();
    Parameter[] parameters = method.getParameters();
    if(parameters!=null && parameters.length>0){
      for (Parameter parameter : parameters) {
        if(parameter.getType().isAssignableFrom(HttpServletRequest.class)) {
          objects.add(req);
        } else if (parameter.getType().isAssignableFrom(HttpServletResponse.class)) {
          objects.add(resp);
        } else {
          objects.add(null);
        }
      }

    }
    return objects;
  }
}
