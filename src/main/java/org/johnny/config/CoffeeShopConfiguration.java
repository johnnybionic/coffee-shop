package org.johnny.config;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.util.Assert;

import java.net.UnknownHostException;

/**
 * Configuration for runtime (non-test).
 *
 * Created by johnny on 17/07/2016.
 */

@Profile("!junit")
@Configuration
public class CoffeeShopConfiguration {

    @Autowired
    private MongoDbFactory mongo;

    @Value("${database-name}")
    private String dbName;

    @Bean
    public DB mongoDB() throws UnknownHostException {
        //MongoClient mongoClient = new MongoClient();
        //DB db = mongoClient.getDB("johnny-coffee");
        Assert.notNull(dbName);
        return mongo.getDb(dbName);
    }

}
