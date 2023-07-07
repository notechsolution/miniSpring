package com.minis.context;

import com.minis.beans.factory.ConfigurableBeanFactory;
import com.minis.beans.factory.ConfigurableListableBeanFactory;
import com.minis.beans.factory.ListableBeanFactory;

public interface ApplicationContext extends ListableBeanFactory, ConfigurableBeanFactory, ApplicationEventPublisher {

  String getApplicationName();
  long getStartupDate();
  ConfigurableListableBeanFactory getBeanFactory();
  void refresh();
  void close();
  boolean isActive();
}
