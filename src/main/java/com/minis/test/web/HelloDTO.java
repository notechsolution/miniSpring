package com.minis.test.web;

public class HelloDTO {


  private String name;
  private String department;
  private String project;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDepartment() {
    return department;
  }

  public void setDepartment(String department) {
    this.department = department;
  }

  public String getProject() {
    return project;
  }

  public void setProject(String project) {
    this.project = project;
  }

  @Override
  public String toString() {
    return "HelloDTO{" +
        "name='" + name + '\'' +
        ", department='" + department + '\'' +
        ", project='" + project + '\'' +
        '}';
  }
}
