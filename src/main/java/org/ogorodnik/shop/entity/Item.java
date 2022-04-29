package org.ogorodnik.shop.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Item {
    private long id;
    private String name;
    private double price;
    private LocalDateTime creationDate;
    private String description;

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", creationDate=" + creationDate +
                ", description=" + description +
                '}';
    }
}
