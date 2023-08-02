package com.minis.web;

import jakarta.servlet.http.HttpServletRequest;

public class WebDataBinderFactory {

  public static WebDataBinder createBinder(HttpServletRequest servletRequest, Object target, String objectName){
    WebDataBinder webDataBinder = new WebDataBinder(target, objectName);
    initBinder(webDataBinder, servletRequest);
    return webDataBinder;
  }

  private static void initBinder(WebDataBinder webDataBinder, HttpServletRequest servletRequest) {

  }

}
