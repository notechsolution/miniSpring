package com.minis.web.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface HandlerAdapter {

  void handler(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException;
}
