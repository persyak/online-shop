package org.ogorodnik.shop.service;

import lombok.RequiredArgsConstructor;
import org.ogorodnik.shop.dao.UserDao;

import java.util.List;

@RequiredArgsConstructor
public class UserService {
    private final UserDao userDao;

    public List<String> getUserPassword(String name) {
        return userDao.getUserPassword(name);
    }
}
