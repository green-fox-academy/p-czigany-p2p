package com.greenfox.p2pchat.service;

import static com.greenfox.p2pchat.P2pchatApplication.CHAT_APP_UNIQUE_ID;

import com.greenfox.p2pchat.dataaccess.RepoHandler;
import com.greenfox.p2pchat.model.Client;
import com.greenfox.p2pchat.model.Log;
import com.greenfox.p2pchat.model.Message;
import com.greenfox.p2pchat.model.ReceivedMessage;
import com.greenfox.p2pchat.model.StatusError;
import com.greenfox.p2pchat.model.StatusOk;
import com.greenfox.p2pchat.model.User;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

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

  private void basicLog(HttpServletRequest request) {
    log.setDateAndTime(new Timestamp(System.currentTimeMillis()));
    log.setMethod(request.getMethod());
    log.setPath(request.getServletPath());
    log.setRequestData(mapToString(request.getParameterMap()));
    System.out.println(log);
  }

  private void infoLog(HttpServletRequest request) {
//    if (System.getenv("CHAT_APP_LOGLEVEL").equals("INFO")) {
    log.setLogLevel("INFO");
    basicLog(request);
//    }
  }

  private void errorLog(HttpServletRequest request) {
    log.setLogLevel("ERROR");
    basicLog(request);
  }

  public String index(Model model, HttpServletRequest request) {
    infoLog(request);
    model.addAttribute("newmessage", new Message());
    model.addAttribute("messages", repoHandler.allMessages());
    if ((repoHandler.allUsers() != null) && (repoHandler.allUsers().size() > 0)) {
      model.addAttribute("user", repoHandler.firstUser());
      return "index";
    } else {
      return "redirect:/enter";
    }
  }

  public String enter(Model model, HttpServletRequest request) {
    infoLog(request);
    model.addAttribute("user", new User());
    return "enter";
  }

  public String enterbutton(User user, HttpServletRequest request) {
    if (user.getUsername().equals("")) {
      errorLog(request);
      return "enterError";
    }
    infoLog(request);
    if (repoHandler.allUsers().size() == 0) {
      repoHandler.saveUser(user);
      return "redirect:/";
    } else if (repoHandler.userByName(user.getUsername()) != null) {
      return "redirect:/";
    } else {
      return "redirect:/enter";
    }
  }


  public String updatebutton(User user, HttpServletRequest request) {
    if (user.getUsername().equals("")) {
      errorLog(request);
      return "indexError";
    }
    infoLog(request);
    repoHandler.updateUsername(repoHandler.firstUser(), user.getUsername());
    return "redirect:/";
  }

  private Message findIdForNewMessage() {
    boolean unique = false;
    Message newMessage = new Message();
    while (!unique) {
      newMessage = new Message();
      if (repoHandler.messageById(newMessage.getId()) == null) {
        unique = true;
      }
    }
    return newMessage;
  }

  public String sendMessage(Message message, HttpServletRequest request) {
    infoLog(request);
    Message newMessage = findIdForNewMessage();
    newMessage.setText(message.getText());
    newMessage.setUsername(repoHandler.firstUser().getUsername());
    repoHandler.saveMessage(newMessage);

    ReceivedMessage toSend = new ReceivedMessage();
    Client me = new Client();
    me.setId(CHAT_APP_UNIQUE_ID);
    toSend.setClient(me);
    toSend.setMessage(newMessage);

    String url = "https://peertopeerchatapp.herokuapp.com/api/message/receive";
    RestTemplate restTemplate = new RestTemplate();
    restTemplate.postForObject(url, toSend, ReceivedMessage.class);

    return "redirect:/";
  }

  private void saveReceivedMessage(Message message) {
    Message newMessage = findIdForNewMessage();
    newMessage.setText(message.getText());
    newMessage.setUsername(message.getUsername());
    repoHandler.saveMessage(newMessage);
  }

  public ResponseEntity<?> receiveMessage(ReceivedMessage receivedMessage,
          HttpServletRequest request) {
    infoLog(request);
    String missingValues = "";
    boolean complete = true;
    if (receivedMessage.getClient().getId() == null) {
      missingValues += "client.id ";
      complete = false;
    }
    if (receivedMessage.getMessage().getText() == null) {
      missingValues += "message.text ";
      complete = false;
    }
    if (receivedMessage.getMessage().getTimestamp() == null) {
      missingValues += "message.timestamp ";
      complete = false;
    }
    if (receivedMessage.getMessage().getUsername() == null) {
      missingValues += "message.username ";
      complete = false;
    }
    if (complete) {
      saveReceivedMessage(receivedMessage.getMessage());
      return new ResponseEntity<>(new StatusOk(), HttpStatus.OK);
    }
    return new ResponseEntity<>(new StatusError("Missing field(s): " + missingValues),
            HttpStatus.BAD_REQUEST);
  }

  private String mapToString(Map<String, String[]> stringPairs) {
    String textualized = "";
    for (String key :
            stringPairs.keySet()) {
      textualized += key + "=" + stringPairs.get(key).toString() + " ";
    }
    return textualized;
  }

  public void deleteUserById(long id) {
    repoHandler.deleteUserById(id);
  }
}
