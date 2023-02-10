package org.ogorodnik.shop.dao;

import org.ogorodnik.shop.security.EncryptedPassword;

import java.util.Optional;

public interface UserDao {
    Optional<EncryptedPassword> getUserPassword(String name);
}
