package xyz.qjex.olstats.collector.workers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.qjex.olstats.entity.Submission;
import xyz.qjex.olstats.entity.User;
import xyz.qjex.olstats.plaforms.Platform;
import xyz.qjex.olstats.plaforms.Platforms;
import xyz.qjex.olstats.repos.SubmissionRepository;

import java.util.List;


/**
 * Created by qjex on 8/9/16.
 */
public class Worker implements Runnable {

    private static Logger logger = LoggerFactory.getLogger(Worker.class.getName());

    private SubmissionRepository repository;

    private Platforms platforms;

    private User user;

    public Worker(User user, SubmissionRepository repository, Platforms platforms) {
        this.user = user;
        this.repository = repository;
        this.platforms = platforms;
    }

    @Override
    public void run() {
        long count = 0;
        for (Platform platform : platforms.getAll()) {
            List<Submission> submissions = platform.getAllSubmissions(user);
            if (submissions == null) continue;
            String handle = user.getId(platform.getIdDescriptor());
            for (Submission submission : submissions) {
                if (repository.countByTaskNameAndPlatformNameAndUserId(submission.getTaskName(), platform.getName(), handle) > 0) continue;
                count++;
                repository.save(submission);
            }
        }
        logger.info("Added " + count + " submissions to " + user.getName());


    }
}
