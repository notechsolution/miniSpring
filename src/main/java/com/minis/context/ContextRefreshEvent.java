package com.minis.context;

public class ContextRefreshEvent extends ApplicationEvent{

  /**
   * Constructs a prototypical Event.
   *
   * @param source the object on which the Event initially occurred
   * @throws IllegalArgumentException if source is null
   */
  public ContextRefreshEvent(Object source) {
    super(source);
  }

  @Override
  public String toString() {
    return this.msg;
  }
}
