package org.ogorodnik.shop.service;

import lombok.RequiredArgsConstructor;
import org.ogorodnik.shop.dao.UserDao;
import org.ogorodnik.shop.security.EncryptedPassword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserService {

    @Autowired
    private final UserDao userDao;

    public EncryptedPassword getUserPassword(String name) {
        return userDao.getUserPassword(name);
    }
}
