package org.ogorodnik.shop.security;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Credentials {
    String name;
    String password;
}
