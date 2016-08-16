package xyz.qjex.olstats.web.response;

import xyz.qjex.olstats.entity.User;


/**
 * Created by qjex on 8/16/16.
 */
public class StatsData {

    private String userId;
    private String name;
    private long count;

    public StatsData(User user, long cnt) {
        userId = user.getUserId();
        name = user.getName();
        count = cnt;
    }

    public long getCount() {
        return count;
    }

    public String getName() {
        return name;
    }

    public String getUserId() {
        return userId;
    }
}
