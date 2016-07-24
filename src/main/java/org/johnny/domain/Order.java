package org.johnny.domain;

import lombok.Data;
import org.mongojack.Id;
import org.mongojack.ObjectId;

/**
 * Represents an order for a coffee.
 *
 * Created by johnny on 17/07/2016.
 */
@Data
public class Order {
    // use MongoDb ObjectID, not String
    @ObjectId
    @Id
    private String id;

    private Long coffeeShopId;
    private String size;
    private String drinker;
    private DrinkType type;
    private String[] selectedOptions;
}
