package org.johnny.controller;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.johnny.domain.Order;
import org.mongojack.JacksonDBCollection;
import org.mongojack.WriteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;


/**
 * Service controller.
 *
 * Created by johnny on 17/07/2016.
 */
@RestController
@RequestMapping("/service")
public class CoffeeShopController {

    private static final int MAX_DISTANCE = 2000;
    private final DB database;

    @Autowired
    public CoffeeShopController(DB database) {
        this.database = database;
    }

    @RequestMapping(value = "/coffeeshop/{id}/order", method = RequestMethod.POST)
    public ResponseEntity saveOrder(@PathVariable Long id, @RequestBody Order order) throws URISyntaxException {

        order.setCoffeeShopId(id);

        DBCollection orders = database.getCollection("orders");
        JacksonDBCollection<Order, String> collection = JacksonDBCollection.wrap(orders, Order.class, String.class);
        WriteResult<Order, String> insert = collection.insert(order);
        if (insert == null) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        order.setId(insert.getSavedId());
        URI location = new URI("");
        //location.create(order.getId());

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setLocation(location);

        return new ResponseEntity<Order>(order, responseHeaders, HttpStatus.CREATED);
    }

    // /service/coffeeshop/{{coffeShopId}}/order/orderId
    @RequestMapping("/coffeeshop/{coffeeShopId}/order/{orderId}")
    public ResponseEntity getOrder(@PathVariable(value = "coffeeShopId") Long coffeeShopId,
                                   @PathVariable(value = "orderId") String orderId, HttpServletRequest request)
            throws URISyntaxException {
        BasicDBObject query = new BasicDBObject();
        query.put("_id", new ObjectId(orderId));

        DBCollection orders = database.getCollection("orders");
        JacksonDBCollection<Order, String> collection = JacksonDBCollection.wrap(orders, Order.class, String.class);
        Order order = collection.findOne(query);

        String url = request.getRequestURL().toString();
        URI location = new URI(url);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setLocation(location);

        return new ResponseEntity<Order>(order, responseHeaders, HttpStatus.OK);
    }

    /*
    * Pattern required on last parameter to prevent Spring treating it as an extension e.g. .xml, .json
     */
    @RequestMapping("/coffeeshop/nearest/{latitude}/{longitude:.*}")
    public ResponseEntity<String> getNearest(@PathVariable(value = "latitude") Double latitude, @PathVariable(value = "longitude") Double longitude) {

        DBCollection coffeeshops = database.getCollection("coffeeshops");

        DBObject one = coffeeshops.findOne(new BasicDBObject("location",
                new BasicDBObject("$near",
                        new BasicDBObject("$geometry",
                                new BasicDBObject("type", "Point")
                                        .append("coordinates", Arrays.asList(longitude, latitude)))
                        .append("$maxDistance", MAX_DISTANCE))));

        if (one == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        String json = one.toString();
        return new ResponseEntity<String>(json, null, HttpStatus.OK);
    }
}
