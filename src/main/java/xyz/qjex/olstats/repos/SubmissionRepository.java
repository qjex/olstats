package xyz.qjex.olstats.repos;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import xyz.qjex.olstats.entity.Submission;

/**
 * Created by qjex on 8/11/16.
 */
public interface SubmissionRepository extends MongoRepository<Submission, String> {

    long countByTaskNameAndPlatformNameAndUserId(String taskName, String platformName, String user);

    @Query(value = "{'userId' : ?0, 'platformName' : ?1, 'date' : {'$gte' : ?2, '$lte' : ?3}}", count = true)
    long countSubmissions(String userId, String platformName, long startTime, long endTime) ;

}
