package org.ogorodnik.shop.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Builder
@Getter
@ToString
public class Item {
    private long id;
    private String name;
    private double price;
    private LocalDateTime creationDate;
    private String description;
}
