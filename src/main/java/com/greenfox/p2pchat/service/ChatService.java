package com.greenfox.p2pchat.service;

import com.greenfox.p2pchat.model.Log;
import java.sql.Timestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by peter on 2017.05.17..
 */
@Component
public class ChatService {

  @Autowired
  Log log;

  public ChatService() {
  }

  public String index() {
    if (System.getenv("CHAT_APP_LOGLEVEL").equals("INFO")) {
      log.setDateAndTime(new Timestamp(System.currentTimeMillis()));
      log.setLogLevel("INFO");
      log.setMethod("GET");
      log.setPath("/");
      log.setRequestData("");
      System.out.println(log);
    }
    return "index";
  }
}
