package com.greenfox.p2pchat.dataaccess;

import com.greenfox.p2pchat.model.User;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by peter on 2017.05.17..
 */
public interface UserRepo extends CrudRepository<User, Long> {

  List<User> findAll();

  User findOneByUsername(String username);

  User findOneById(long id);
}
