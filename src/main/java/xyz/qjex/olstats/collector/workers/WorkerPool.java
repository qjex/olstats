package xyz.qjex.olstats.collector.workers;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import xyz.qjex.olstats.entity.User;
import xyz.qjex.olstats.plaforms.Platforms;
import xyz.qjex.olstats.repos.SubmissionRepository;

import javax.annotation.PostConstruct;
import java.util.concurrent.*;

/**
 * Created by qjex on 8/9/16.
 */
@Service
public class WorkerPool {

    @Autowired
    private Logger logger;

    @Autowired
    private Platforms platforms;

    @Autowired
    private SubmissionRepository submissionRepository;

    private ExecutorService pool;
    private int poolSize;

    @Autowired
    public WorkerPool(@Value("${collector.pool.size}") final String sz) {
        this.poolSize = Integer.valueOf(sz);
    }

    @PostConstruct
    public void init() {
        logger.info("Starting worker pool");
        RejectedExecutionHandler rejectedExecutionHandler = new ThreadPoolExecutor.CallerRunsPolicy();
        pool = new ThreadPoolExecutor(poolSize / 2, poolSize, 10 * 60, TimeUnit.SECONDS, new ArrayBlockingQueue<> (100), rejectedExecutionHandler);
    }

    public void process(User user) {
        pool.submit(new Worker(user, submissionRepository, platforms));
    }

    public void shutdown() {
        logger.info("Terminating worker pool");
        pool.shutdown();
        try {
            if (!pool.awaitTermination(60, TimeUnit.SECONDS)) {
                pool.shutdownNow();
            }
        } catch (InterruptedException ie) {
            pool.shutdownNow();
        }
    }

}
