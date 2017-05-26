package com.greenfox.p2pchat.controller;

import com.greenfox.p2pchat.model.Message;
import com.greenfox.p2pchat.model.User;
import com.greenfox.p2pchat.service.ChatService;
import com.greenfox.p2pchat.service.Logger;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by peter on 2017.05.17..
 */
@Controller
public class Main {

  @Autowired
  private Logger logger;

  @Autowired
  private ChatService chatService;

  @GetMapping(value = "/")
  public String index(@RequestParam(value = "emptyfield", required = false) boolean emptyfield, Model model, HttpServletRequest request) {
    logger.infoLog(request);
    return chatService.index(model, emptyfield);
  }

  @GetMapping(value = "/enter")
  public String enter(@RequestParam(value = "emptyfield", required = false) boolean emptyfield, Model model, HttpServletRequest request) {
    logger.infoLog(request);
    return chatService.enter(model, emptyfield);
  }

  @PostMapping(value = "/enterbutton")
  public String enterbutton(User user, HttpServletRequest request) {
    logger.infoLog(request);
    return chatService.enterbutton(user);
  }

  @PostMapping(value = "/updatebutton")
  public String updatebutton(User user, HttpServletRequest request) {
    logger.infoLog(request);
    return chatService.updatebutton(user);
  }

  @PostMapping(value = "/sendbutton")
  public String sendbutton(Message message, HttpServletRequest request) {
    logger.infoLog(request);
    return chatService.sendMessage(message);
  }
}
