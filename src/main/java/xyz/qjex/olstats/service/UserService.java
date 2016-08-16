package xyz.qjex.olstats.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.qjex.olstats.entity.User;
import xyz.qjex.olstats.entity.UserList;
import xyz.qjex.olstats.repos.UserListRepository;
import xyz.qjex.olstats.repos.UserRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qjex on 8/16/16.
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserListRepository userListRepository;

    public UserList getUserList(String id) {
        return userListRepository.findById(id);
    }

    public User getUser(String id) {
        return userRepository.findByUserId(id);
    }


    public List<User> findByUserList(UserList userList) {
        List<User> result = new ArrayList<>();
        for (String id : userList.getUserId()) {
            result.add(getUser(id));
        }
        return result;
    }
}
