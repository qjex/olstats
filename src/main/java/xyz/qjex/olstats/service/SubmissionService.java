package xyz.qjex.olstats.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.qjex.olstats.collector.workers.WorkerPool;
import xyz.qjex.olstats.entity.Submission;
import xyz.qjex.olstats.entity.User;
import xyz.qjex.olstats.plaforms.Platform;
import xyz.qjex.olstats.repos.SubmissionRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qjex on 8/16/16.
 */
@Service
public class SubmissionService {

    @Autowired
    private WorkerPool workerPool;

    @Autowired
    private SubmissionRepository submissionRepository;
    public void updateUserSubmissions(User user) {
        workerPool.process(user);
    }

    public long countSubmissions(User user, List<Platform> platforms, long startTime, long endTime) {
        long result = 0;
        for (Platform platform : platforms) {
            result += submissionRepository.countSubmissions(user.getUserId(), platform.getName(), startTime, endTime);
        }
        return result;
    }

    public long countCustomSubmissions(User user, List<Platform> platforms, long startTime, long endTime) {
        return getCustomSubmissions(user, platforms, startTime, endTime).size();
    }

    public List<Submission> getCustomSubmissions(User user, List<Platform> platforms, long startTime, long endTime) {
        List<Submission> result = new ArrayList<>();
        for (Platform platform : platforms) {
            List<Submission> submissions = platform.getAllSubmissions(user);
            if (submissions == null) continue;
            for (Submission submission : submissions) {
                long date = submission.getDate();
                if (date >= startTime && date <= endTime) {
                    result.add(submission);
                }
            }
        }
        return result;
    }

    public List<Submission> getSubmissions(User user, List<Platform> platforms, long startTime, long endTime) {
        List<Submission> result = new ArrayList<>();

        return result;
    }
}
