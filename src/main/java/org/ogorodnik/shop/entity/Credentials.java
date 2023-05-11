package org.ogorodnik.shop.entity;

import lombok.*;
import org.springframework.data.annotation.Id;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Credentials {
    @Id
    private Long id;
    private String login;
    private String password;
    private String salt;
}
