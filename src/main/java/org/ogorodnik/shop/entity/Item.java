package org.ogorodnik.shop.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Please add item name")
    @Length(min = 3)
    private String name;
    @Positive(message = "Value must be above zero")
    @NotNull(message = "Value can't be blank")
    private Double price;
    @Builder.Default
    private LocalDateTime creationDate = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
    private String description;
}
