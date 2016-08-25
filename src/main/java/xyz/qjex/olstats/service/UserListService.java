package xyz.qjex.olstats.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.qjex.olstats.collector.workers.WorkerPool;
import xyz.qjex.olstats.entity.UserList;
import xyz.qjex.olstats.repos.UserListRepository;

/**
 * Created by qjex on 8/24/16.
 */
@Service
public class UserListService {

    @Autowired
    private UserListRepository repository;

    @Autowired
    private WorkerPool workerPool;

    public void update(UserList userList) {
//        if (userList.getId() == null) {
//            workerPool.
//        }
        repository.save(userList);
    }

    public void delete(String id) {
        repository.delete(id);
    }
}
