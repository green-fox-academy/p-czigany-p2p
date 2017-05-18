package com.greenfox.p2pchat.service;

import com.greenfox.p2pchat.dataaccess.RepoHandler;
import com.greenfox.p2pchat.model.Log;
import com.greenfox.p2pchat.model.User;
import java.sql.Timestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

/**
 * Created by peter on 2017.05.17..
 */
@Component
public class ChatService {

  @Autowired
  private Log log;

  @Autowired
  private RepoHandler repoHandler;

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

  public String enter(Model model) {
    if (System.getenv("CHAT_APP_LOGLEVEL").equals("INFO")) {
      log.setDateAndTime(new Timestamp(System.currentTimeMillis()));
      log.setLogLevel("INFO");
      log.setMethod("GET");
      log.setPath("/enter");
      log.setRequestData("");
      System.out.println(log);
    }
    model.addAttribute("user", new User());
    return "enter";
  }

  public String newUser(User user) {
    repoHandler.saveUser(user);
    return "redirect:/";
  }
}
