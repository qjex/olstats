package xyz.qjex.olstats.web.response;

import xyz.qjex.olstats.entity.CustomUser;
import xyz.qjex.olstats.entity.User;

import java.util.Map;


/**
 * Created by qjex on 8/16/16.
 */
public class StatsData {

    private String userId;
    private String name;
    private Map<String, Long> count;
    private long totalCount;

    public StatsData(User user, Map<String, Long> count) {
        userId = user.getUserId();
        name = user.getName();
        for (Map.Entry<String, Long> ent : count.entrySet()) {
            totalCount += ent.getValue();
        }
        this.count = count;
    }

    public StatsData(CustomUser user, Map<String, Long> count) {
        userId = user.getUser().getUserId();
        name = user.getName();
        for (Map.Entry<String, Long> ent : count.entrySet()) {
            totalCount += ent.getValue();
        }
        this.count = count;
    }

    public Map<String, Long> getCount() {
        return count;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public String getName() {
        return name;
    }

    public String getUserId() {
        return userId;
    }
}
