package org.ogorodnik.shop.dao.jdbc.util;

import org.slf4j.Logger;

import java.sql.SQLException;

public class JdbcUtil {

    public static void handleSqlException(SQLException throwable, Logger log){
        log.error("Error Code = {} ", throwable.getErrorCode());
        log.error("SQL state =  {} ", throwable.getSQLState());
        log.error("Message = {} ", throwable.getMessage());
        throwable.printStackTrace();
    }
}
