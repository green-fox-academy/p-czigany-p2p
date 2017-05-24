package com.greenfox.p2pchat.controller;

import com.greenfox.p2pchat.model.ReceivedMessage;
import com.greenfox.p2pchat.model.StatusError;
import com.greenfox.p2pchat.model.StatusOk;
import com.greenfox.p2pchat.service.ChatService;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by peter on 2017.05.22..
 */
@RestController
public class RestControl {

  @Autowired
  private ChatService chatService;

  @CrossOrigin("*")
  @PostMapping(value = "/api/message/receive")
  public ResponseEntity<?> receiveMessage(@RequestBody ReceivedMessage receivedMessage, HttpServletRequest request) {
    return chatService.receiveMessage(receivedMessage, request);
  }
}
