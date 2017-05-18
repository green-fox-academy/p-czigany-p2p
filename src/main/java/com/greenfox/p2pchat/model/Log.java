package com.greenfox.p2pchat.model;

import java.sql.Timestamp;
import org.springframework.stereotype.Component;

/**
 * Created by peter on 2017.05.17..
 */
@Component
public class Log {

  private String path;
  private String method;
  private Timestamp dateAndTime;
  private String logLevel;
  private String requestData;

  public Log() {
  }

  public String getPath() {
    return path;
  }

  public String getMethod() {
    return method;
  }

  public Timestamp getDateAndTime() {
    return dateAndTime;
  }

  public String getLogLevel() {
    return logLevel;
  }

  public String getRequestData() {
    return requestData;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public void setMethod(String method) {
    this.method = method;
  }

  public void setDateAndTime(Timestamp dateAndTime) {
    this.dateAndTime = dateAndTime;
  }

  public void setLogLevel(String logLevel) {
    this.logLevel = logLevel;
  }

  public void setRequestData(String requestData) {
    this.requestData = requestData;
  }

  @Override
  public String toString() {
    return String.valueOf(dateAndTime) + " "
            + logLevel + " "
            + "Request " + path + " "
            + method + " "
            + requestData;
  }
}
