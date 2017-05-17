package com.greenfox.p2pchat.controller;

import com.greenfox.p2pchat.model.User;
import com.greenfox.p2pchat.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by peter on 2017.05.17..
 */
@Controller
public class Main {

  @Autowired
  ChatService chatService;

  @GetMapping(value = "/")
  public String index() {
    return chatService.index();
  }

  @GetMapping(value = "/enter")
  public String enter() {

    return "enter";
  }

  @GetMapping(value = "/enterbutton")
  public String enterbutton(User user) {
    return chatService.newUser(user);
  }
}
