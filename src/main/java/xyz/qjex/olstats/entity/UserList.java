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
    private List<User> users;

    public UserList() {
        users = new ArrayList<>();
    }

    public UserList(String name) {
        this.name = name;
        users = new ArrayList<>();
    }

    public List<User> getUsers() {
        return users;
    }

    public String getId() {
        return id;
    }

    public void addUser(User user) {
        users.add(user);
    }

    public String getName() {
        return name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
