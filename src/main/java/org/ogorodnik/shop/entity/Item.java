package org.ogorodnik.shop.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class Item {
    private long id;
    private String name;
    private double price;
    private LocalDateTime creationDate;
    private String description;
}
