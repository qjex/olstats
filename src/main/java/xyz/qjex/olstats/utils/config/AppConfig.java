package xyz.qjex.olstats.utils.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import xyz.qjex.olstats.Application;
import xyz.qjex.olstats.collector.Collector;
import xyz.qjex.olstats.collector.workers.WorkerPool;
import xyz.qjex.olstats.plaforms.*;

/**
 * Created by qjex on 8/11/16.
 */

@Configuration
@PropertySource("file:./config.properties")
public class AppConfig {

    @Autowired
    private Environment env;

    @Bean
    public Collector getCollector() {
        return new Collector(getWorkerPool());
    }

    @Bean(destroyMethod = "shutdown")
    public WorkerPool getWorkerPool() {
        return new WorkerPool(env.getProperty("collector.pool.size"));
    }

    @Bean
    public Logger getLogger(){
        return LoggerFactory.getLogger("application");
    }

    @Bean
    public Platforms getPlatforms() {
        PlatformsBuilder pb = new PlatformsBuilder();
        pb.addPlatform(new Acmp());
        pb.addPlatform(new Codeforces());
        pb.addPlatform(new Informatics());
        return pb.build();
    }

}
