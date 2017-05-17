package com.greenfox.p2pchat.controller;

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

  @GetMapping(value = {"/", "/index"})
  public String index() {
    return chatService.index();
  }
}
