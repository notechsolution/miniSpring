package com.minis.web;

import com.minis.core.ClassPathXmlResource;
import com.minis.core.Resource;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class DispatcherServlet extends HttpServlet {

  private Map<String, MapppingValue> mappingValues;
  private Map<String, Class<?>> mappingClazz = new HashMap<>();
  private Map<String, Object> mappingObjects = new HashMap<>();

  @Override
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    String contextConfigLocation = config.getInitParameter("contextConfigLocation");
    URL xmlPath = null;
    try {
      xmlPath = getServletContext().getResource(contextConfigLocation);
    } catch (MalformedURLException e) {
      throw new RuntimeException(e);
    }
    Resource resource = new ClassPathXmlResource(xmlPath);
    XmlMVCConfigReader reader = new XmlMVCConfigReader();
    mappingValues = reader.loadConfig(resource);
    refresh();
  }

  protected void refresh() {
    for (Entry<String, MapppingValue> entry : mappingValues.entrySet()) {
      String id = entry.getKey();
      String className = entry.getValue().clz;

      try {
        Class<?> clazz = Class.forName(className);
        Object object = clazz.getConstructor().newInstance();
        mappingClazz.put(id, clazz);
        mappingObjects.put(id, object);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }

  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String path = req.getServletPath();
    if (mappingValues.get(path) == null) {
      System.out.println("[MiniWeb]No Controller found for the path:" + path);
      return;
    }
    Class<?> clazz = mappingClazz.get(path);
    String methodName = mappingValues.get(path).getMethod();
    Object controller = mappingObjects.get(path);
    Object result = null;
    try {
      Method method = clazz.getMethod(methodName);
      result = method.invoke(controller);
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      throw new RuntimeException(e);
    }
    if (result != null) {
      resp.getWriter().append(result.toString());
    } else {
      resp.getWriter().append("no result from controller");
    }

    System.out.println("[MiniWeb] Handle path "+path +" complete");
  }
}
