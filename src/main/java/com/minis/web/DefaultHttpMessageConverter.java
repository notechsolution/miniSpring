package com.minis.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DefaultHttpMessageConverter implements HttpMessageConverter {

  private ObjectMapper objectMapper;
  String defaultContentType = "text/json;charset=UTF-8";
  String defaultCharacterEncoding = "UTF-8";

  public DefaultHttpMessageConverter(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  public void write(Object obj, HttpServletResponse response) throws IOException {
    response.setContentType(defaultContentType);
    response.setCharacterEncoding(defaultCharacterEncoding);
    writeInternal(obj, response);

  }

  private void writeInternal(Object obj, HttpServletResponse response) throws IOException {
    String jsonString = objectMapper.writeValueAsString(obj);
    response.getWriter().write(jsonString);
  }
}
