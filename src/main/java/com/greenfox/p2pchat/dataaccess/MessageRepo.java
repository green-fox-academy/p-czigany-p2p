package com.greenfox.p2pchat.dataaccess;

import com.greenfox.p2pchat.model.Message;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by peter on 2017.05.17..
 */
public interface MessageRepo extends CrudRepository<Message, Long> {

  List<Message> findAllOrderByTimestamp();

  Message findOneById(long id);
}
