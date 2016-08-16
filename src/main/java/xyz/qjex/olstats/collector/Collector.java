package xyz.qjex.olstats.collector;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import xyz.qjex.olstats.collector.workers.WorkerPool;
import xyz.qjex.olstats.entity.User;
import xyz.qjex.olstats.repos.UserRepository;

import java.util.List;

/**
 * Created by qjex on 8/9/16.
 */
@PropertySource("file:./config.properties")
public class Collector {

    @Autowired
    private Logger logger;

    @Autowired
    private UserRepository userRepository;

    private WorkerPool workerPool;

    public Collector(WorkerPool workerPool) {
        this.workerPool = workerPool;
    }

    @Scheduled(fixedDelayString = "${update_rate:600}000")
    private void collect() {
        List<User> userList = userRepository.findAll();
        for (User user : userList) {
            logger.info("Updating user " + user.getUserId() + " (" + user.getName() + ")");
            workerPool.process(user);
        }


    }


}
