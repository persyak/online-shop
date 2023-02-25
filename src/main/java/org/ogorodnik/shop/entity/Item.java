package org.ogorodnik.shop.entity;

import lombok.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Item {
    long id;
    private String name;
    private double price;
    @Builder.Default
    private LocalDateTime creationDate = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
    private String description;
}
