package xyz.qjex.olstats.repos;

import org.springframework.data.mongodb.repository.MongoRepository;
import xyz.qjex.olstats.entity.UserList;

/**
 * Created by qjex on 8/11/16.
 */
public interface UserListRepository extends MongoRepository<UserList, String> {

    UserList findById(String id);

    UserList findByName(String name);

    UserList countByName(String name);

    UserList countById(String id);

}
