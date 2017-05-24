package com.greenfox.p2pchat.model;

/**
 * Created by peter on 2017.05.22..
 */
public class StatusOk {

  private String status;

  public StatusOk() {
    status = "ok";
  }

  public StatusOk(String status) {
    this.status = status;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}
