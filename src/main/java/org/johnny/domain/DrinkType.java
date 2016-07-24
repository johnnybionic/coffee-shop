package org.johnny.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Drink type.
 *
 * Created by johnny on 17/07/2016.
 */
@Data
@EqualsAndHashCode
@ToString
public class DrinkType {

    private String name;
    private String family;
}
