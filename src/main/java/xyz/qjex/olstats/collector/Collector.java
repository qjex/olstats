package xyz.qjex.olstats.collector;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import xyz.qjex.olstats.collector.workers.WorkerPool;
import xyz.qjex.olstats.entity.User;
import xyz.qjex.olstats.entity.UserList;
import xyz.qjex.olstats.repos.UserListRepository;

import java.util.List;

/**
 * Created by qjex on 8/9/16.
 */
@PropertySource("file:./config.properties")
public class Collector {

    @Autowired
    private Logger logger;

    @Autowired
    private UserListRepository repository;

    private WorkerPool workerPool;

    public Collector(WorkerPool workerPool) {
        this.workerPool = workerPool;
    }

    @Scheduled(fixedDelayString = "${update_rate:600}000")
    private void collect() {
        List<UserList> userLists = repository.findAll();
        for (UserList userList : userLists) {
            for (User user : userList.getUsers()) {
                logger.info("Updating user " + user.getName());
                workerPool.process(user);
            }
        }


    }


}
