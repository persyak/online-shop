package org.ogorodnik.shop.service;

import lombok.AllArgsConstructor;
import org.ogorodnik.shop.dao.UserDao;

import java.util.List;

@AllArgsConstructor
public class UserService {
    private final UserDao userDao;

    public List<String> getUserPassword(String name) {
        return userDao.getUserPassword(name);
    }
}
