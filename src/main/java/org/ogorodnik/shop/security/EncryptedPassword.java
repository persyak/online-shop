package org.ogorodnik.shop.security;

import lombok.Builder;
import lombok.Getter;
import lombok.Value;

@Builder
@Getter
@Value
public class EncryptedPassword {
    String password;
    String salt;

    public EncryptedPassword(String password, String salt) {
        this.password = password;
        this.salt = salt;
    }
}
