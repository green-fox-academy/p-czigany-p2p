package com.greenfox.p2pchat.model;

import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by peter on 2017.05.17..
 */
@Entity
public class Message {

  @Id
  private long id; // (between 1000000 - 9999999)
  private String username;
  private String text;
  private Timestamp timestamp;

  public Message() {
    id = (long) (Math.random() * 10000000) + 1000000;
    timestamp = new Timestamp(System.currentTimeMillis());
  }

  public long getId() {
    return id;
  }

  public String getUsername() {
    return username;
  }

  public String getText() {
    return text;
  }

  public Timestamp getTimestamp() {
    return timestamp;
  }

  public void setId(long id) {
    this.id = id;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setText(String text) {
    this.text = text;
  }

  public void setTimestamp(Timestamp timestamp) {
    this.timestamp = timestamp;
  }
}
