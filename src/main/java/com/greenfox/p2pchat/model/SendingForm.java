package com.greenfox.p2pchat.model;

/**
 * Created by peter on 2017.05.22..
 */
public class SendingForm {

  private Message message;
  private Client client;

  public SendingForm() {
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
