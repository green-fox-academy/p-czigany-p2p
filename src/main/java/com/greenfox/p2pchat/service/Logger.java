package com.greenfox.p2pchat.service;

import com.greenfox.p2pchat.model.Log;
import java.sql.Timestamp;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by peter on 2017.05.25..
 */
@Component
public class Logger {

  @Autowired
  private Log log;

  public Logger() {
  }

  private void basicLog(HttpServletRequest request) {
    log.setDateAndTime(new Timestamp(System.currentTimeMillis()));
    log.setMethod(request.getMethod());
    log.setPath(request.getServletPath());
    log.setRequestData(mapToString(request.getParameterMap()));
    System.out.println(log);
  }

  public void infoLog(HttpServletRequest request) {
    if (System.getenv("CHAT_APP_LOGLEVEL").equals("INFO")) {
      log.setLogLevel("INFO");
      basicLog(request);
    }
  }

  public void errorLog(HttpServletRequest request) {
    log.setLogLevel("ERROR");
    basicLog(request);
  }

  private String mapToString(Map<String, String[]> paramAndValues) {
    String textualized = "";
    int i = 0;
    for (String key :
            paramAndValues.keySet()) {
      if (paramAndValues.get(key).length > 1) {
        for (int j = 0; j < paramAndValues.get(key).length; j++) {
          textualized += key + "(" + (j + 1) + ")=" + paramAndValues.get(key)[j];
          if (j < paramAndValues.get(key).length - 1) {
            textualized += ", ";
          }
        }
      }
      if (paramAndValues.get(key).length == 1) {
        textualized += key + "=" + paramAndValues.get(key)[0];
        if (i < paramAndValues.keySet().size() - 1) {
          textualized += ", ";
        }
      }
      i++;
    }
    return textualized;
  }
}
