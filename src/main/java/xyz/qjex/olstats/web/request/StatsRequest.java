package xyz.qjex.olstats.web.request;

import xyz.qjex.olstats.entity.User;

import java.util.List;

/**
 * Created by qjex on 8/16/16.
 */
public class StatsRequest {

    private List<String> sites, userLists;
    private List<User> customUsers;
    private long startTime;
    private long endTime;

    public List<String> getSites() {
        return sites;
    }

    public List<String> getUserLists() {
        return userLists;
    }

    public List<User> getCustomUsers() {
        return customUsers;
    }

    public long getEndTime() {
        return endTime;
    }

    public long getStartTime() {
        return startTime;
    }

}
