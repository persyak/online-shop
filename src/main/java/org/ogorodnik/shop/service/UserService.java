package org.ogorodnik.shop.service;

import lombok.RequiredArgsConstructor;
import org.ogorodnik.shop.dao.UserDao;
import org.ogorodnik.shop.security.EncryptedPassword;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDao userDao;

    public Optional<EncryptedPassword> getUserPassword(String login) {
        return userDao.getUserPassword(login);
    }
}
