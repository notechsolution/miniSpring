package com.minis.web;

public class MapppingValue {

  String url;
  String clz;
  String method;

  public MapppingValue(String url, String clz, String method) {
    this.url = url;
    this.clz = clz;
    this.method = method;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getClz() {
    return clz;
  }

  public void setClz(String clz) {
    this.clz = clz;
  }

  public String getMethod() {
    return method;
  }

  public void setMethod(String method) {
    this.method = method;
  }
}
