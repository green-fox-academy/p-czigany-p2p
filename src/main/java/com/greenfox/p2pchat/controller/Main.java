package com.greenfox.p2pchat.controller;

import com.greenfox.p2pchat.model.Message;
import com.greenfox.p2pchat.model.User;
import com.greenfox.p2pchat.service.ChatService;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Created by peter on 2017.05.17..
 */
@Controller
public class Main {

  @Autowired
  private ChatService chatService;

  @GetMapping(value = "/")
  public String index(Model model, HttpServletRequest request) {
    return chatService.index(model, request);
  }

  @GetMapping(value = "/enter")
  public String enter(Model model, HttpServletRequest request) {
    return chatService.enter(model, request);
  }

  @PostMapping(value = "/enterbutton")
  public String enterbutton(User user, HttpServletRequest request) {
    return chatService.enterbutton(user, request);
  }

  @PostMapping(value = "/updatebutton")
  public String updatebutton(User user, HttpServletRequest request) {
    return chatService.updatebutton(user, request);
  }

  @PostMapping(value = "/sendbutton")
  public String sendbutton(Message message, HttpServletRequest request) {
    return chatService.sendMessage(message, request);
  }
}
