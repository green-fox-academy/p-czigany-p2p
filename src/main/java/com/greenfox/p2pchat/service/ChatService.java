package com.greenfox.p2pchat.service;

import com.greenfox.p2pchat.dataaccess.RepoHandler;
import com.greenfox.p2pchat.model.Log;
import com.greenfox.p2pchat.model.Message;
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

  public String index(Model model) {
//    if (System.getenv("CHAT_APP_LOGLEVEL").equals("INFO")) {
    log.setDateAndTime(new Timestamp(System.currentTimeMillis()));
    log.setLogLevel("INFO");
    log.setMethod("GET");
    log.setPath("/");
    log.setRequestData("");
    System.out.println(log);
//    }
    model.addAttribute("newmessage", new Message());
    model.addAttribute("models", repoHandler.allMessages());
    if ((repoHandler.allUsers() != null) && (repoHandler.allUsers().size() > 0)) {
      model.addAttribute("user", repoHandler.firstUser());
      return "index";
    } else {
      return "redirect:/enter";
    }
  }

  public String enter(Model model) {
//    if (System.getenv("CHAT_APP_LOGLEVEL").equals("INFO")) {
    log.setDateAndTime(new Timestamp(System.currentTimeMillis()));
    log.setLogLevel("INFO");
    log.setMethod("GET");
    log.setPath("/enter");
    log.setRequestData("");
    System.out.println(log);
//    }
    model.addAttribute("user", new User());
    return "enter";
  }

  public String enterbutton(User user) {
    if (user.getUsername().equals("")) {
      log.setDateAndTime(new Timestamp(System.currentTimeMillis()));
      log.setLogLevel("ERROR");
      log.setMethod("SET");
      log.setPath("/enterbutton");
      log.setRequestData("error=Missing username.");
      System.out.println(log);
      return "enterError";
    } else {
//      if (System.getenv("CHAT_APP_LOGLEVEL").equals("INFO")) {
      log.setDateAndTime(new Timestamp(System.currentTimeMillis()));
      log.setLogLevel("INFO");
      log.setMethod("SET");
      log.setPath("/enterbutton");
      log.setRequestData("username=" + user.getUsername());
      System.out.println(log);
//      }
      if (repoHandler.allUsers().size() == 0) {
        repoHandler.saveUser(user);
        return "redirect:/";
      } else if (repoHandler.userByName(user.getUsername()) != null) {
        return "redirect:/";
      } else {
        return "redirect:/enter";
      }
    }
  }

  public String updatebutton(User user) {
    if (user.getUsername().equals("")) {
      log.setDateAndTime(new Timestamp(System.currentTimeMillis()));
      log.setLogLevel("ERROR");
      log.setMethod("SET");
      log.setPath("/updatebutton");
      log.setRequestData("error=Missing username.");
      System.out.println(log);
      return "indexError";
    } else {
//      if (System.getenv("CHAT_APP_LOGLEVEL").equals("INFO")) {
      log.setDateAndTime(new Timestamp(System.currentTimeMillis()));
      log.setLogLevel("INFO");
      log.setMethod("SET");
      log.setPath("/updatebutton");
      log.setRequestData("newname=" + user.getUsername());
      System.out.println(log);
//      }
      repoHandler.updateUsername(repoHandler.firstUser(), user.getUsername());
      return "redirect:/";
    }
  }

  public String saveMessage(Message message) {
    boolean unique = false;
    Message newMessage = new Message();
    while (!unique) {
      newMessage = new Message();
      if (repoHandler.messageById(newMessage.getId()) == null) {
        unique = true;
      }
    }
    newMessage.setText(message.getText());
    newMessage.setUsername(repoHandler.firstUser().getUsername());
    repoHandler.saveMessage(newMessage);
    return "redirect:/";
  }

  public void deleteUserById(long id) {
    repoHandler.deleteUserById(id);
  }
}
