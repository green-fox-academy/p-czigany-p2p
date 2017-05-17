package com.greenfox.p2pchat.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by peter on 2017.05.17..
 */
@Entity
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  private String username;

  public User() {

  }

  public long getId() {
    return id;
  }

  public String getUsername() {
    return username;
  }

  public void setId(long id) {
    this.id = id;
  }

  public void setUsername(String username) {
    this.username = username;
  }
}
