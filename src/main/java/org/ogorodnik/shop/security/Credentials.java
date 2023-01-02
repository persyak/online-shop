package org.ogorodnik.shop.security;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Credentials {
    private final String userName;
    private final String password;
}
