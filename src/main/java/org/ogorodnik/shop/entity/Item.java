package org.ogorodnik.shop.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Item {
    @Id
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
