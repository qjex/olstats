package xyz.qjex.olstats.web.controller;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import xyz.qjex.olstats.entity.User;
import xyz.qjex.olstats.entity.UserList;
import xyz.qjex.olstats.plaforms.Platform;
import xyz.qjex.olstats.plaforms.Platforms;
import xyz.qjex.olstats.repos.UserListRepository;
import xyz.qjex.olstats.service.SubmissionService;
import xyz.qjex.olstats.service.UserService;
import xyz.qjex.olstats.web.request.StatsRequest;
import xyz.qjex.olstats.web.response.DefaultResponse;
import xyz.qjex.olstats.web.response.StatsData;

import java.util.*;

/**
 * Created by qjex on 8/16/16.
 */
@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private Logger logger;

    private final Platforms platforms;
    private final UserListRepository userListRepository;
    private final SubmissionService submissionService;
    private final UserService userService;

    @Autowired
    public ApiController(SubmissionService submissionService, Platforms platforms,
                         UserListRepository userListRepository, UserService userService) {
        this.submissionService = submissionService;
        this.platforms = platforms;
        this.userListRepository = userListRepository;
        this.userService = userService;
    }

    @RequestMapping(value = "/get_stats", method = RequestMethod.POST, produces = "application/json")
    public DefaultResponse getStats(@RequestBody StatsRequest request) {
        List<StatsData> response = new ArrayList<>();
        List<Platform> allPlatforms = new ArrayList<>();

        Set<String> ids = new HashSet<>();
        for (String name : request.getSites()) {
            Platform platform = platforms.getByName(name);
            if (ids.contains(name)) continue;
            allPlatforms.add(platform);
            ids.add(name);
        }

        long startTime = request.getStartTime();
        long endTime = request.getEndTime();

        for (User user : request.getCustomUsers()) {
            long cnt = submissionService.countCustomSubmissions(user, allPlatforms, startTime, endTime);
            response.add(new StatsData(user, cnt));
        }

        ids.clear();
        for (String id : request.getUserLists()) {
            UserList userList = userListRepository.findById(id);
            if (userList == null) continue;
            List<User> users = userService.findByUserList(userList);
            for (User user : users) {
                String userId = user.getUserId();
                if (ids.contains(userId)) continue;
                long cnt = submissionService.countSubmissions(user, allPlatforms, startTime, endTime);
                response.add(new StatsData(user, cnt));
                ids.add(userId);
            }
        }
        return new DefaultResponse(response);
    }





}