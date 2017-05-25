package com.greenfox.p2pchat.service;

import static com.greenfox.p2pchat.P2pchatApplication.CHAT_APP_PEER_ADDRESS;
import static com.greenfox.p2pchat.P2pchatApplication.CHAT_APP_UNIQUE_ID;

import com.greenfox.p2pchat.dataaccess.RepoHandler;
import com.greenfox.p2pchat.model.Client;
import com.greenfox.p2pchat.model.Log;
import com.greenfox.p2pchat.model.Message;
import com.greenfox.p2pchat.model.SendingForm;
import com.greenfox.p2pchat.model.StatusError;
import com.greenfox.p2pchat.model.StatusOk;
import com.greenfox.p2pchat.model.User;
import java.sql.Timestamp;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
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
    }
    return "redirect:/enter";
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
    }
    if (repoHandler.userByName(user.getUsername()) != null) {
      return "redirect:/";
    }
    return "redirect:/enter";

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

  public String sendMessage(Message myMessage, HttpServletRequest request) {
    infoLog(request);
    forwardForm(prepareMyMessageToForward(myMessage));
    saveSentMessage(myMessage);
    return "redirect:/";
  }

  public SendingForm prepareMyMessageToForward(Message myMessage) {
    SendingForm toSend = new SendingForm();
    Client me = new Client();
    me.setId(CHAT_APP_UNIQUE_ID);
    toSend.setClient(me);
    toSend.setMessage(myMessage);
    return toSend;
  }

  public void forwardForm(SendingForm toSend) {
    String url = CHAT_APP_PEER_ADDRESS;
    RestTemplate restTemplate = new RestTemplate();
    try {
      restTemplate.postForObject(url, toSend, SendingForm.class);
    } catch (Exception e) {
//      errorLog(request);
      System.out.println("Couldn't send message!");
    }
  }

  private void saveSentMessage(Message message) {
    saveMessage(message, repoHandler.firstUser().getUsername());
  }

  private void saveReceivedMessage(Message message) {
    saveMessage(message, message.getUsername());
  }

  private void saveMessage(Message message, String username) {
    Message newMessage = findIdForNewMessage();
    newMessage.setText(message.getText());
    newMessage.setUsername(username);
    repoHandler.saveMessage(newMessage);
  }

  public ResponseEntity<?> receiveMessage(SendingForm receivedForm,
          HttpServletRequest request) {
    infoLog(request);

    String missingValues = "";
    boolean complete = true;
    if (receivedForm.getClient().getId() == null) {
      missingValues += "client.id";
      complete = false;
    }
    if (receivedForm.getMessage().getText() == null) {
      if (!complete) {
        missingValues += ", ";
      }
      missingValues += "message.text";
      complete = false;
    }
    if (receivedForm.getMessage().getTimestamp() == null) {
      if (!complete) {
        missingValues += ", ";
      }
      missingValues += "message.timestamp";
      complete = false;
    }
    if (receivedForm.getMessage().getUsername() == null) {
      if (!complete) {
        missingValues += ", ";
      }
      missingValues += "message.username";
      complete = false;
    }

    if (complete) {

      if (!receivedForm.getClient().getId().equals(CHAT_APP_UNIQUE_ID)) {

        forwardForm(receivedForm);

        saveReceivedMessage(receivedForm.getMessage());
      }

      return new ResponseEntity<>(new StatusOk(), HttpStatus.OK);
    }
    return new ResponseEntity<>(new StatusError("Missing field(s): " + missingValues),
            HttpStatus.BAD_REQUEST);
  }

  private String mapToString(Map<String, String[]> stringPairs) {
    String textualized = "";
    int i = 0;
    for (String key :
            stringPairs.keySet()) {
      if (stringPairs.get(key).length > 1) {
        for (int j = 0; j < stringPairs.get(key).length; j++) {
          textualized += key + "(" + (j + 1) + ")=" + stringPairs.get(key)[j];
          if (j < stringPairs.get(key).length - 1) {
            textualized += ", ";
          }
        }
      }
      if (stringPairs.get(key).length == 1) {
        textualized += key + "=" + stringPairs.get(key)[0];
        if (i < stringPairs.keySet().size() - 1) {
          textualized += ", ";
        }
      }
      i++;
    }
    return textualized;
  }

  public void deleteUserById(long id) {
    repoHandler.deleteUserById(id);
  }
}
