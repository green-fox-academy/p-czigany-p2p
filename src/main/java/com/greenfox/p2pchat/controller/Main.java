package com.greenfox.p2pchat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by peter on 2017.05.17..
 */
@Controller
public class Main {

  @RequestMapping("/")
  public String index() {
    return "index";
  }
}
