package org.ogorodnik.shop.security;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Credentials {
    private final String name;
    private final String password;
}
