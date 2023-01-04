package org.ogorodnik.shop.dao;

import org.ogorodnik.shop.security.EncryptedPassword;

public interface UserDao {
    EncryptedPassword getUserPassword(String name);
}
