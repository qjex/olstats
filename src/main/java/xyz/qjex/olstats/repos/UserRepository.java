package xyz.qjex.olstats.repos;

import org.springframework.data.mongodb.repository.MongoRepository;
import xyz.qjex.olstats.entity.User;

/**
 * Created by qjex on 8/11/16.
 */
public interface UserRepository extends MongoRepository<User, String> {
}
