package org.ogorodnik.shop.dao.jdbc.util;

import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;

@Slf4j
public class JdbcUtil {

    public static void handleSqlException(SQLException throwable) {
        log.error("Error Code = {} ", throwable.getErrorCode());
        log.error("SQL state =  {} ", throwable.getSQLState());
        log.error("Message = {} ", throwable.getMessage());
    }
}
