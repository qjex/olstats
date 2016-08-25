package xyz.qjex.olstats.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.qjex.olstats.collector.workers.WorkerPool;
import xyz.qjex.olstats.entity.Submission;
import xyz.qjex.olstats.entity.User;
import xyz.qjex.olstats.plaforms.Platform;
import xyz.qjex.olstats.repos.SubmissionRepository;

import java.util.*;

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
            String handle = user.getId(platform.getIdDescriptor());
            result += submissionRepository.countSubmissions(handle, platform.getName(), startTime, endTime);
        }
        return result;
    }

    public Map<String, Long> countSubmissionsByPlatform(User user, List<Platform> platforms, long startTime, long endTime) {
        Map<String, Long> result = new HashMap<>();
        for (Platform platform : platforms) {
            String name = platform.getName();
            String handle = user.getId(platform.getIdDescriptor());
            result.put(name, submissionRepository.countSubmissions(handle, name, startTime, endTime));
        }
        return result;
    }

//    public long countCustomSubmissions(User user, List<Platform> platforms, long startTime, long endTime) {
//        return getCustomSubmissions(user, platforms, startTime, endTime).size();
//    }

    public Map<String, Long> countCustomSubmissionsByPlatform(User user, List<Platform> platforms, long startTime, long endTime) {
        Map<String, Long> result = new HashMap<>();
        for (Platform platform : platforms) {
            String name = platform.getName();
            List<Submission> cur = getCustomSubmissions(user, platform, startTime, endTime);
            if (cur == null) continue;
            result.put(name, (long)cur.size());
        }
        return result;
    }

    public List<Submission> getCustomSubmissions(User user, Platform platform, long startTime, long endTime) {
        List<Submission> result = new ArrayList<>();
        if (user == null) return result;
        List<Submission> submissions = platform.getAllSubmissions(user);
        if (submissions == null) return result;
        Set<String> tasks = new TreeSet<>();
        for (Submission submission : submissions) {
            long date = submission.getDate();
            if (date >= startTime && date <= endTime) {
                if (tasks.contains(submission.getTaskName())) continue;
                result.add(submission);
                tasks.add(submission.getTaskName());
            }
        }
        return result;
    }

    public List<Submission> getSubmissions(User user, List<Platform> platforms, long startTime, long endTime) {
        List<Submission> result = new ArrayList<>();
        //TODO
        return result;
    }
}
