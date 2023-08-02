package com.minis.web.servlet;

import com.minis.web.WebApplicationContext;
import com.minis.web.WebDataBinder;
import com.minis.web.WebDataBinderFactory;
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
    Object returnObject = null;
    try {
      Method method = handler.getMethod();
      System.out.println(method.getParameters().length);
      List<Object> arguments = prepareMethodArgumentObjects(method, request, response);
      if(arguments.size() == 0) {
        returnObject = method.invoke(controller);
      } else {
        returnObject = method.invoke(controller, arguments.toArray());
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    if (returnObject != null) {
      response.getWriter().append(returnObject.toString());
    } else {

      response.getWriter().append("no result from controller");
    }
  }

  private List<Object> prepareMethodArgumentObjects(Method method, HttpServletRequest req, HttpServletResponse resp)
      throws InstantiationException, IllegalAccessException {
    List<Object> methodParameterObjects = new ArrayList<>();
    Parameter[] parameters = method.getParameters();
    if(parameters!=null && parameters.length>0){
      for (Parameter parameter : parameters) {
//        if(parameter.getType().isAssignableFrom(HttpServletRequest.class)) {
//          methodParameterObjects.add(req);
//        } else if (parameter.getType().isAssignableFrom(HttpServletResponse.class)) {
//          methodParameterObjects.add(resp);
//        } else {
//          methodParameterObjects.add(null);
//        }
        Object methodParamObj = parameter.getType().newInstance();
        WebDataBinder dataBinder = WebDataBinderFactory.createBinder(req, methodParamObj, parameter.getName());
        dataBinder.bind(req);
        methodParameterObjects.add(methodParamObj);
      }

    }
    return methodParameterObjects;
  }
}
