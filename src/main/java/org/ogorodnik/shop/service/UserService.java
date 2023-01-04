package org.ogorodnik.shop.service;

import lombok.RequiredArgsConstructor;
import org.ogorodnik.shop.dao.UserDao;
import org.ogorodnik.shop.security.EncryptedPassword;

@RequiredArgsConstructor
public class UserService {
    private final UserDao userDao;

    public EncryptedPassword getUserPassword(String name) {
        return userDao.getUserPassword(name);
    }
}
