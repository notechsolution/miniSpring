package com.minis.context;

import java.util.EventObject;

public class ApplicationEvent extends EventObject {

  protected String msg = null;

  /**
   * Constructs a prototypical Event.
   *
   * @param source the object on which the Event initially occurred
   * @throws IllegalArgumentException if source is null
   */
  public ApplicationEvent(Object source) {
    super(source);
    this.msg = source.toString();
  }
}
