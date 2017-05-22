package com.greenfox.p2pchat;

import java.util.Map;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class P2pchatApplication {

//  Environmental Variables:
  public static final String CHAT_APP_LOGLEVEL = "INFO";
  public static final String CHAT_APP_UNIQUE_ID = "p-czigany";
  public static final String CHAT_APP_PEER_ADDRESSS = "url of the next person's application";

  public static void main(String[] args) {
//	  Set Environmental Variables:
    ProcessBuilder pb = new ProcessBuilder("myCommand", "myArg1", "myArg2");
    Map<String, String> env = pb.environment();
    env.put("CHAT_APP_LOGLEVEL", CHAT_APP_LOGLEVEL);
//    Run Application:
		SpringApplication.run(P2pchatApplication.class, args);
	}
}
