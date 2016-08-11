package xyz.qjex.olstats.repos;

import org.springframework.data.mongodb.repository.MongoRepository;
import xyz.qjex.olstats.entity.Submission;

/**
 * Created by qjex on 8/11/16.
 */
public interface SubmissionRepository extends MongoRepository<Submission, String> {

    long countByTaskNameAndPlatformNameAllIgnoreCase(String taskName, String platformName);

}
