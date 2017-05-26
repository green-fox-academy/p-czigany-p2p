package com.greenfox.p2pchat.controller;

import com.google.gson.Gson;
import com.greenfox.p2pchat.P2pchatApplication;
import com.greenfox.p2pchat.model.Client;
import com.greenfox.p2pchat.model.Message;
import com.greenfox.p2pchat.model.SendingForm;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
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

  private void performPost(MockMvc mockMvc, String url, String sentJson, String expectJson) throws Exception {
    mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON_UTF8).content(sentJson))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(content().json(expectJson));
  }

  @Test
  public void answerMessageSuccessfully() throws Exception {
    mockMvc.perform(post("/api/message/receive")
            .contentType(MediaType.APPLICATION_JSON_UTF8).content("{"
                    + "  \"message\": {"
                    + "    \"id\": 7655482,"
                    + "    \"username\": \"EggDice\","
                    + "    \"text\": \"How you doin'?\","
                    + "    \"timestamp\": 1322018752992"
                    + "  },"
                    + "  \"client\": {"
                    + "    \"id\": \"EggDice\""
                    + "  }"
                    + "}"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(content().json("{\"status\": \"ok\"}"));
  }

  @Test
  public void answerMessageWithMissingText() throws Exception {
    mockMvc.perform(post("/api/message/receive")
            .contentType(MediaType.APPLICATION_JSON_UTF8).content("{"
                    + "  \"message\": {"
                    + "    \"id\": 7655482,"
                    + "    \"username\": \"EggDice\","
//                    + "    \"text\": \"How you doin'?\","
                    + "    \"timestamp\": 1322018752992"
                    + "  },"
                    + "  \"client\": {"
                    + "    \"id\": \"EggDice\""
                    + "  }"
                    + "}"))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.status", is("error")))
            .andExpect(jsonPath("$.message", is("Missing field(s): message.text")));
  }
}
