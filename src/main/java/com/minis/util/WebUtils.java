package com.minis.util;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class WebUtils {

  public static Map<String, Object> getParametersStartingWith(HttpServletRequest request, String prefix) {
    Map<String, Object> params = new HashMap<>();
    Enumeration<String> paramNames = request.getParameterNames();
    String _prefix = prefix;
    if (_prefix == null) {
      _prefix = "";
    }
    while (paramNames !=null && paramNames.hasMoreElements()) {
      String paramName = paramNames.nextElement();
      if(_prefix.isEmpty() || paramName.startsWith(_prefix)) {
        params.put(paramName, request.getParameter(paramName));
      }
    }
    return params;
  }

}
