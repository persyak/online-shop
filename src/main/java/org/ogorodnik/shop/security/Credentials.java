package org.ogorodnik.shop.security;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@RequiredArgsConstructor
@Data
@Value
public class Credentials {
    String name;
    String password;
}
