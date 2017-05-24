package com.greenfox.p2pchat.model;

/**
 * Created by peter on 2017.05.22..
 */
public class ReceivedMessage {

  private Message message;
  private Client client;

  public ReceivedMessage() {
  }

  public Message getMessage() {
    return message;
  }

  public Client getClient() {
    return client;
  }

  public void setMessage(Message message) {
    this.message = message;
  }

  public void setClient(Client client) {
    this.client = client;
  }
}
