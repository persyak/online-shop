package org.ogorodnik.shop.entity;

import lombok.*;
import org.springframework.data.annotation.Id;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Credentials {
    @Id
    Long id;
    String login;
    String password;
    String salt;
}
