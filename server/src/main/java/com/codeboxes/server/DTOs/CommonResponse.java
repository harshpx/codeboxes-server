package com.codeboxes.server.DTOs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonResponse<T> {
  private int status;
  private long timeStamp = System.currentTimeMillis();
  private boolean success;
  private T response;

  // constructor
  public CommonResponse() {}
  public CommonResponse(T data) {
    // if only data is passed, treat as success
    this.response = data;
    this.status = 200;
    this.success = true;
  }
  public CommonResponse(T data, int status) {
    // if data and status is passed set success accordingly
    this.response = data;
    this.status = status;
    if (status >= 400) this.success = false;
    else this.success = true;
  }
  public CommonResponse(T data, int status, boolean success) {
    // if everything is passed, set accordingly
    this.response = data;
    this.status = status;
    this.success = success;
  }

  // getters & setters will be produced by lombok
}