package com.minis.web.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minis.beans.PropertyEditorRegistrySupport;
import com.minis.beans.factory.config.PropertyEditor;
import com.minis.util.ClassUtils;
import com.minis.web.DefaultHttpMessageConverter;
import com.minis.web.HttpMessageConverter;
import com.minis.web.WebApplicationContext;
import com.minis.web.WebDataBinder;
import com.minis.web.WebDataBinderFactory;
import com.minis.web.annotation.RequestParam;
import com.minis.web.annotation.ResponseBody;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

public class RequestMappingHandlerAdapter implements HandlerAdapter {

  WebApplicationContext webApplicationContext;
  HttpMessageConverter messageConverter;

  public RequestMappingHandlerAdapter(WebApplicationContext webApplicationContext) {
    this(webApplicationContext, new DefaultHttpMessageConverter(new ObjectMapper()));
  }

  public RequestMappingHandlerAdapter(WebApplicationContext webApplicationContext, HttpMessageConverter messageConverter) {
    this.webApplicationContext = webApplicationContext;
    this.messageConverter = messageConverter;
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
      List<Object> arguments = prepareMethodArgumentObjects(method, request, response);
      if (arguments.size() == 0) {
        returnObject = method.invoke(controller);
      } else {
        returnObject = method.invoke(controller, arguments.toArray());
      }

      if (method.isAnnotationPresent(ResponseBody.class)) {
        this.messageConverter.write(returnObject, response);
      } else if (returnObject != null) {
        response.getWriter().append("no result from controller");
      } else {
        response.getWriter().append(returnObject.toString());
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private List<Object> prepareMethodArgumentObjects(Method method, HttpServletRequest req, HttpServletResponse resp)
      throws InstantiationException, IllegalAccessException {
    List<Object> methodParameterObjects = new ArrayList<>();
    Parameter[] parameters = method.getParameters();
    if (parameters != null && parameters.length > 0) {
      for (Parameter parameter : parameters) {
        if (parameter.getType().isAssignableFrom(HttpServletRequest.class)) {
          methodParameterObjects.add(req);
        } else if (parameter.getType().isAssignableFrom(HttpServletResponse.class)) {
          methodParameterObjects.add(resp);
        } else if (ClassUtils.isPrimitiveOrWrapper(parameter.getType()) || parameter.getType().isAssignableFrom(String.class)) {
          if (parameter.isAnnotationPresent(RequestParam.class)) {
            String paramName = parameter.getAnnotation(RequestParam.class).value();
            PropertyEditorRegistrySupport propertyEditorSupport = new PropertyEditorRegistrySupport();
            propertyEditorSupport.registerDefaultEditors();
            PropertyEditor propertyEditor = propertyEditorSupport.getDefaultEditor(parameter.getType());
            propertyEditor.setAsText(req.getParameter(paramName));
            methodParameterObjects.add(propertyEditor.getValue());
          } else {
            methodParameterObjects.add(null);
          }

        } else {
          Object methodParamObj = parameter.getType().newInstance();
          WebDataBinder dataBinder = WebDataBinderFactory.createBinder(req, methodParamObj, parameter.getName());
          dataBinder.bind(req);
          methodParameterObjects.add(methodParamObj);
        }

      }

    }
    return methodParameterObjects;
  }
}
