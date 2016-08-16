package xyz.qjex.olstats.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qjex on 8/16/16.
 */
public class UserList {

    @Id
    private String id;
    private String name;
    private List<String> userId;

    public UserList() {
        userId = new ArrayList<>();
    }

    public List<String> getUserId() {
        return userId;
    }

    public String getId() {
        return id;
    }

    public void addUser(User user) {
        userId.add(user.getUserId());
    }

    public String getName() {
        return name;
    }
}
