package com.minis.web;

import com.minis.beans.BeansException;
import com.minis.beans.factory.config.Autowired;
import com.minis.core.ClassPathXmlResource;
import com.minis.core.Resource;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DispatcherServlet extends HttpServlet {

  private Map<String, Class<?>> mappingClazz = new HashMap<>();
  private Map<String, Object> mappingObjects = new HashMap<>();
  private List<String> packageNames = new ArrayList<>();
  private List<String> controllerNames = new ArrayList<>();
  Map<String, Class<?>> controllerClazz = new HashMap<>();
  Map<String, Object> controllerObjects = new HashMap<>();

  Map<String, Method> mappingMethods = new HashMap<>();
  List<String> urlMappingNames = new ArrayList<>();

  private WebApplicationContext webApplicationContext;

  @Override
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    this.webApplicationContext = (WebApplicationContext) getServletContext().getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
    String contextConfigLocation = config.getInitParameter("contextConfigLocation");
    URL xmlPath = null;
    try {
      xmlPath = getServletContext().getResource(contextConfigLocation);
    } catch (MalformedURLException e) {
      throw new RuntimeException(e);
    }
    Resource resource = new ClassPathXmlResource(xmlPath);
    this.packageNames = XmlScanComponentHelper.getNodeValue(resource);
    refresh();
  }

  protected void refresh() {
    initController();
    initMapping();
    System.out.println("[miniMVC] load paths: "+urlMappingNames);
  }

  private void initMapping() {
    for (String controllerName : controllerNames) {
      Class<?> clazz = controllerClazz.get(controllerName);
      Object object = controllerObjects.get(controllerName);
      Method[] methods = clazz.getDeclaredMethods();
      for (Method method : methods) {
        if(method.isAnnotationPresent(RequestMapping.class)) {
          String mappingUrl = method.getAnnotation(RequestMapping.class).value();
          this.mappingObjects.put(mappingUrl, object);
          this.mappingMethods.put(mappingUrl, method);
          this.urlMappingNames.add(mappingUrl);
        }
      }

    }

  }

  private void initController() {
    controllerNames = scanPackages(this.packageNames);
    for (String controllerName : controllerNames) {
      try {
        Class<?> clazz = Class.forName(controllerName);
        controllerClazz.put(controllerName, clazz);
        Object object = clazz.getConstructor().newInstance();
        // combined IoC and MVC
        populateBean(object, controllerName);
        controllerObjects.put(controllerName, object);
      } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
        throw new RuntimeException(e);
      } catch (BeansException e) {
        throw new RuntimeException(e);
      }
    }

  }

  private void populateBean(Object bean, String controllerName) throws BeansException {
    Class<? extends Object> clazz = bean.getClass();
    Field[] fields = clazz.getDeclaredFields();
    if (fields != null) {
      for (Field field : fields) {
        boolean isAutowired = field.isAnnotationPresent(Autowired.class);
        if(isAutowired){
          System.out.println("field "+field.getName() +" isAutowired "+ isAutowired);
          String fieldName = field.getName();
          Object autowiredObject = this.webApplicationContext.getBean(fieldName);
          try {
            field.setAccessible(true);
            field.set(bean, autowiredObject);
            System.out.println("autowire "+ fieldName +" for bean "+ controllerName);
          } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
          }
        }
      }

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
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String path = req.getServletPath();
    if (!urlMappingNames.contains(path)) {
      System.out.println("[MiniWeb]No Controller found for the path:" + path);
      resp.sendError(HttpServletResponse.SC_NOT_FOUND, "No Controller found for the path");
      return;
    }
    Object controller = mappingObjects.get(path);
    Object result = null;
    try {
      Method method = mappingMethods.get(path);
      System.out.println(method.getParameters().length);
      List<Object>arguments = prepareArguments(method, req, resp);
      if(arguments.size() == 0) {
        result = method.invoke(controller);
      } else {
        result = method.invoke(controller, arguments.toArray());
      }
    } catch (IllegalAccessException | InvocationTargetException e) {
      throw new RuntimeException(e);
    }
    if (result != null) {
      resp.getWriter().append(result.toString());
    } else {

      resp.getWriter().append("no result from controller");
    }

    System.out.println("[MiniWeb] Handle path " + path + " complete");
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
