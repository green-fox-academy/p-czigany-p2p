package com.greenfox.p2pchat.controller;

import com.greenfox.p2pchat.model.SendingForm;
import com.greenfox.p2pchat.service.ChatService;
import com.greenfox.p2pchat.service.Logger;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by peter on 2017.05.22..
 */
@RestController
public class Rest {

  @Autowired
  private Logger logger;

  @Autowired
  private ChatService chatService;

  @CrossOrigin("*")
  @PostMapping(value = "/api/message/receive")
  public ResponseEntity<?> receiveMessage(@RequestBody SendingForm sendingForm, HttpServletRequest request) {
    logger.infoLog(request);
    return chatService.receiveMessage(sendingForm);
  }
}
