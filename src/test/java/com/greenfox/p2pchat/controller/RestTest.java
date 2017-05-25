package com.greenfox.p2pchat.controller;

import com.google.gson.Gson;
import com.greenfox.p2pchat.P2pchatApplication;
import com.greenfox.p2pchat.model.Client;
import com.greenfox.p2pchat.model.Message;
import com.greenfox.p2pchat.model.SendingForm;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Created by peter on 2017.05.24..
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = P2pchatApplication.class)
@WebAppConfiguration
@EnableWebMvc
public class RestTest {

  private MockMvc mockMvc;

  @Autowired
  private WebApplicationContext webApplicationContext;

  @Before
  public void setup() throws Exception {
    this.mockMvc = webAppContextSetup(webApplicationContext).build();
  }

  @Test
  public void receiveMessageSuccessfully() throws Exception {
    Client myClient = new Client();
    myClient.setId("sender");

    Message myMessage = new Message();
    myMessage.setUsername("user");
    myMessage.setText("Dear Everybody! ...");

    SendingForm validMessage = new SendingForm();
    validMessage.setClient(myClient);
    validMessage.setMessage(myMessage);

    Gson gson = new Gson();
    String json = gson.toJson(validMessage);

    System.out.println(json);

    mockMvc.perform(post("/api/message/receive")
            .contentType(MediaType.APPLICATION_JSON_UTF8).content(json))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.status", is("ok")));
  }

  @Test
  public void receceiveMessageWithMissingText() throws Exception {
    Client myClient = new Client();
    myClient.setId("sender");

    Message myMessage = new Message();
    myMessage.setUsername("user");
//    myMessage.setText("Dear Everybody! ...");
    SendingForm validMessage = new SendingForm();
    validMessage.setClient(myClient);
    validMessage.setMessage(myMessage);

    Gson gson = new Gson();
    String json = gson.toJson(validMessage);

    mockMvc.perform(post("/api/message/receive")
            .contentType(MediaType.APPLICATION_JSON_UTF8).content(json))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.status", is("error")))
            .andExpect(jsonPath("$.message", is("Missing field(s): message.text")));
  }
}
