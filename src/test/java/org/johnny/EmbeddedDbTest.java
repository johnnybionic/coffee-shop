package org.johnny;

import static org.junit.Assert.*;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.File;
import java.io.IOException;

/**
 * First steps to an embedded database for unit tests.
 * <p>
 * Created by johnny on 21/07/2016.
 */
@ActiveProfiles({"junit"})
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(CoffeeShopApplication.class)
@WebAppConfiguration
public class EmbeddedDbTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    public void load() {

        importJSON("orders", "src/test/resources/orders.txt");

        importJSON("coffeeshops", "src/test/resources/shops.txt");

        long orders = mongoTemplate.getCollection("orders").count();

        assertNotEquals(0, orders);

    }

    /**
     * I found this approach in a web article
     * http://dontpanic.42.nl/2015/02/in-memory-mongodb-for-unit-and.html
     *
     * However it doesn't read 'real' JSON, instead it's line after line of individual records. Does the job for now.
     * @param collection the DB collection to which the file is saved
     * @param file the file to read
     */
    private void importJSON(String collection, String file) {
        try {
            for (String line : FileUtils.readLines(new File(file), "utf8")) {
                if (line.length() > 0) {
                    mongoTemplate.save(line, collection);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not import file: " + file, e);
        }
    }
}
