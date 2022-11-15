package org.ogorodnik.shop.dao;

import java.util.List;

public interface UserDao {
    List<String> getUserPassword(String name);
}
