package com.greenfox.p2pchat.dataaccess;

import com.greenfox.p2pchat.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by peter on 2017.05.17..
 */
@Component
public class RepoHandler {

  @Autowired
  private UserRepo userRepo;

//  @Autowired
//  private MessageRepo messageRepo;

  public RepoHandler() {

  }

  public void saveUser(User user) {
    userRepo.save(user);
  }

  public Iterable<User> allUsers() {
    return userRepo.findAll();
  }

  public User getUserByName(String nameToFindBy) {
    return userRepo.findByUsername(nameToFindBy);
  }
}
