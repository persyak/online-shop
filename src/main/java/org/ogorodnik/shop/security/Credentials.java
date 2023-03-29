package org.ogorodnik.shop.security;

import lombok.*;
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Credentials {
    String login;
    String password;
    String salt;
}
