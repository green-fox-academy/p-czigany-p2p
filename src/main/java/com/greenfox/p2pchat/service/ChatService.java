package com.greenfox.p2pchat.service;

import com.greenfox.p2pchat.dataaccess.RepoHandler;
import com.greenfox.p2pchat.model.Client;
import com.greenfox.p2pchat.model.Message;
import com.greenfox.p2pchat.model.SendingForm;
import com.greenfox.p2pchat.model.StatusError;
import com.greenfox.p2pchat.model.StatusOk;
import com.greenfox.p2pchat.model.User;
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
  private RepoHandler repoHandler;

  public ChatService() {
  }

  public String index(Model model, Boolean emptyfield) {
    model.addAttribute("emptyfield", emptyfield);
    model.addAttribute("newmessage", new Message());
    model.addAttribute("messages", repoHandler.allMessages());
    if ((repoHandler.allUsers() != null) && (repoHandler.allUsers().size() > 0)) {
      model.addAttribute("user", repoHandler.firstUser());
      return "index";
    }
    return "redirect:/enter";
  }

  public String enter(Model model, Boolean emptyfield) {
    model.addAttribute("emptyfield", emptyfield);
    model.addAttribute("user", new User());
    return "enter";
  }

  public String enterbutton(User user) {
    if (user.getUsername().equals("")) {

      return "redirect:/enter";
    }
    if (repoHandler.allUsers().size() == 0) {
      repoHandler.saveUser(user);
      return "redirect:/";
    }
    if (repoHandler.userByName(user.getUsername()) != null) {
      return "redirect:/";
    }
    return "redirect:/enter";
  }

  public String updatebutton(User user) {
    if (user.getUsername().equals("")) {
      return "index";
    }
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

  public String sendMessage(Message myMessage) {
    myMessage.setUsername(repoHandler.firstUser().getUsername());
    forwardForm(prepareMyMessageToForward(myMessage));
    saveMessage(myMessage);
    return "redirect:/";
  }

  private SendingForm prepareMyMessageToForward(Message myMessage) {
    Client me = new Client();
    me.setId(System.getenv("CHAT_APP_UNIQUE_ID"));
    SendingForm toSend = new SendingForm();
    toSend.setClient(me);
    toSend.setMessage(myMessage);
    return toSend;
  }

  private void forwardForm(SendingForm toSend) {
    String url = System.getenv("CHAT_APP_PEER_ADDRESS") + "/api/message/receive";
    RestTemplate restTemplate = new RestTemplate();
    try {
      restTemplate.postForObject(url, toSend, SendingForm.class);
    } catch (Exception e) {
      System.out.println("Couldn't send message!");
    }
  }

  private void saveMessage(Message message) {
    Message newMessage = findIdForNewMessage();
    newMessage.setText(message.getText());
    newMessage.setUsername(message.getUsername());
    newMessage.setTimestamp(message.getTimestamp());
    repoHandler.saveMessage(newMessage);
  }

  public ResponseEntity<?> receiveMessage(SendingForm receivedForm) {
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

      if (!receivedForm.getClient().getId().equals(System.getenv("CHAT_APP_UNIQUE_ID"))
              && repoHandler.messagesByTimestampAndUser(receivedForm.getMessage().getTimestamp(),
              receivedForm.getMessage().getUsername()).size() == 0) {

        saveMessage(receivedForm.getMessage());

        receivedForm.getMessage().setText(receivedForm.getMessage().getText()
                + System.getenv("CHAT_APP_APPENDIX"));

        forwardForm(receivedForm);
      }

      return new ResponseEntity<>(new StatusOk(), HttpStatus.OK);
    }
    return new ResponseEntity<>(new StatusError("Missing field(s): " + missingValues),
            HttpStatus.BAD_REQUEST);
  }

  public void deleteUserById(long id) {
    repoHandler.deleteUserById(id);
  }
}
