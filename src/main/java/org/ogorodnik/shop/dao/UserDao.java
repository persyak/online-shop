package org.ogorodnik.shop.dao;

import java.sql.SQLException;

public interface UserDao {
    String getUserPassword(String name) throws SQLException;
}
