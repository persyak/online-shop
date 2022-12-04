package org.ogorodnik.shop.security;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class EncryptedPassword {
    private final String password;
    private final String salt;

    public EncryptedPassword(String password, String salt) {
        this.password = password;
        this.salt = salt;
    }
}
