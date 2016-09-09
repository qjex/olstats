package xyz.qjex.olstats.config;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.WriteResultChecking;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import xyz.qjex.olstats.Application;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.singletonList;

/**
 * Created by qjex on 8/11/16.
 */
@EnableMongoRepositories(basePackageClasses = Application.class)
@Configuration
@PropertySource("file:./config.properties")
public class MongoConfig extends AbstractMongoConfiguration {

    @Value("${mongodb.database:olstats}")
    private String dbName;

    @Value("${mongodb.host}")
    private String dbHost;

    @Value("${mongodb.username}")
    private String dbUserName;

    @Value("${mongodb.password}")
    private String dbPassword;

    @Override
    protected String getDatabaseName() {
        return dbName;
    }

    @Override
    public Mongo mongo() throws Exception {
        if (dbUserName == null || dbUserName.length() == 0) return new MongoClient(dbHost);
        return new MongoClient(new ServerAddress(dbHost),
                singletonList(MongoCredential.createCredential(dbUserName, "admin", dbPassword.toCharArray())));
    }

    @Bean
    public MongoTemplate getMongoTemplate() throws Exception {
        MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory(), mappingMongoConverter());
        mongoTemplate.setWriteResultChecking(WriteResultChecking.EXCEPTION);
        return mongoTemplate;
    }


}
