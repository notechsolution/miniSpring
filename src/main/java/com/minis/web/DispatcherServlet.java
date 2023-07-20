package com.minis.web;

import com.minis.core.ClassPathXmlResource;
import com.minis.core.Resource;
import com.minis.web.servlet.HandlerAdapter;
import com.minis.web.servlet.HandlerMethod;
import com.minis.web.servlet.RequestMappingHandlerAdapter;
import com.minis.web.servlet.RequestMappingHandlerMapping;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DispatcherServlet extends HttpServlet {

  private List<String> packageNames = new ArrayList<>();

  public static final String WEB_APPLICATION_CONTEXT_ATTRIBUTE = DispatcherServlet.class.getName() + ".CONTEXT";

  private WebApplicationContext webApplicationContext;
  private WebApplicationContext parentWebApplicationContext;
  private RequestMappingHandlerMapping handlerMapping;
  private HandlerAdapter handlerAdapter;

  @Override
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    this.parentWebApplicationContext = (WebApplicationContext) getServletContext().getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
    String contextConfigLocation = config.getInitParameter("contextConfigLocation");
    URL xmlPath = null;
    try {
      xmlPath = getServletContext().getResource(contextConfigLocation);
    } catch (MalformedURLException e) {
      throw new RuntimeException(e);
    }
    Resource resource = new ClassPathXmlResource(xmlPath);
    this.packageNames = XmlScanComponentHelper.getNodeValue(resource);
    this.webApplicationContext = new AnnotationConfigWebApplicationContext(contextConfigLocation, this.parentWebApplicationContext);
    refresh();
  }

  protected void refresh() {
    initHandlerMappings();
    initHandlerAdapters();
  }

  private void initHandlerAdapters() {
    System.out.println("[miniMVC] Init Handler Adapters");
    this.handlerAdapter = new RequestMappingHandlerAdapter(this.webApplicationContext);
  }

  private void initHandlerMappings() {
    System.out.println("[miniMVC] Init Handler Mappings");
    this.handlerMapping = new RequestMappingHandlerMapping(this.webApplicationContext);
  }


  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp){
    req.setAttribute(WEB_APPLICATION_CONTEXT_ATTRIBUTE, webApplicationContext);
    try {
      doDispatch(req, resp);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

  }

  private void doDispatch(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    String path = req.getServletPath();
    HandlerMethod handler = handlerMapping.getHandler(req);
    if (handler == null) {
      System.out.println("[MiniWeb]No Controller found for the path:" + path);
      resp.sendError(HttpServletResponse.SC_NOT_FOUND, "No Controller found for the path");
      return;
    }
    handlerAdapter.handler(req, resp, handler);
    System.out.println("[MiniWeb] Handle path " + path + " complete");
  }


}
