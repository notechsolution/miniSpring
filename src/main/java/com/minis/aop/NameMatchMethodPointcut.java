package com.minis.aop;

import com.minis.util.PatternMatchUtils;
import java.lang.reflect.Method;

public class NameMatchMethodPointcut implements MethodMatcher, Pointcut{

  private String mappedName = "";


  @Override
  public boolean matches(Method method, Class<?> targetClass) {
    if(mappedName.equalsIgnoreCase(method.getName()) || isMatch(mappedName, method.getName())){
      return true;
    }
    return false;
  }

  private boolean isMatch(String mappedName, String methodName) {
    return PatternMatchUtils.simpleMatch(mappedName, methodName);
  }

  @Override
  public MethodMatcher getMethodMatcher() {
    return this;
  }

  public void setMappedName(String mappedName) {
    this.mappedName = mappedName;
  }
}
