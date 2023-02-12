package org.ogorodnik.shop.security;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Data
@Value
@RequiredArgsConstructor
public class Credentials {
    String name;
    String password;
}
