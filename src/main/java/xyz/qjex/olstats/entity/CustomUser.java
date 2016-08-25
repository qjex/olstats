package xyz.qjex.olstats.entity;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by qjex on 8/19/16.
 */
public class CustomUser {

    private String name;
    private Map<String, String> ids;

    public Map<String, String> getIds() {
        return ids;
    }

    public String getName() {
        return name;
    }

    public User getUser() {
        return new User(name, ids);
    }
}

