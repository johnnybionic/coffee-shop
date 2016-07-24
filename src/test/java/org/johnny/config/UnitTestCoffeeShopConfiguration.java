package org.johnny.config;

import com.mongodb.DB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.util.Assert;

import java.net.UnknownHostException;

//import cz.jirutka.spring.embedmongo.EmbeddedMongoBuilder;

/**
 * Configuration for unit test.
 *
 * Created by johnny on 17/07/2016.
 */

@Profile("junit")
@Configuration
public class UnitTestCoffeeShopConfiguration {

    @Autowired
    private MongoDbFactory mongo;

    @Value("${database-name}")
    private String dbName;

    /*
    This is the same as non-test for now - the aim is to use fakemongo instead of relying on a running instance of MongoDB.
     */
    @Bean
    public DB mongoDB() throws UnknownHostException {
        //MongoClient mongoClient = new MongoClient();
        //DB db = mongoClient.getDB("johnny-coffee");
        Assert.notNull(dbName);
        return mongo.getDb(dbName);
    }

}
