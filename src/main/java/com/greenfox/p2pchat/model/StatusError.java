package com.greenfox.p2pchat.model;

/**
 * Created by peter on 2017.05.22..
 */
public class StatusError {

  private String status;
  private String message;

  public StatusError() {
    status = "error";
  }

  public StatusError(String message) {
    status = "error";
    this.message = message;
  }

  public StatusError(String status, String message) {
    this.status = status;
    this.message = message;
  }

  public String getStatus() {
    return status;
  }

  public String getMessage() {
    return message;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
