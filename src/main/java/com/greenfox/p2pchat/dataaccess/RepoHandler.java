package com.greenfox.p2pchat.dataaccess;

import com.greenfox.p2pchat.model.User;
import java.util.List;
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

  public List<User> allUsers() {
    return userRepo.findAll();
  }

  public User firstUser() {
    return userRepo.findAll().get(0);
  }

  public User userByName(String nameToFindBy) {
    return userRepo.findOneByUsername(nameToFindBy);
  }

  public void updateUsername(User userToUpdate, String newName) {
    userRepo.findOneById(userToUpdate.getId()).setUsername(newName);
    userRepo.save(userToUpdate);
  }

  public void deleteUserById(long id) {
    userRepo.delete(id);
  }
}
