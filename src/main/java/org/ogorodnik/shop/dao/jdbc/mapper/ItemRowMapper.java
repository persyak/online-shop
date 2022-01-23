package org.ogorodnik.shop.dao.jdbc.mapper;

import org.ogorodnik.shop.entity.Item;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class ItemRowMapper {
    public Item mapRow(ResultSet resultSet) throws SQLException {
        Item item = new Item();
        item.setId(resultSet.getLong("id"));
        item.setName(resultSet.getString("name"));
        item.setPrice(resultSet.getDouble("price"));

        Timestamp timeStamp = resultSet.getTimestamp("creationDate");
        LocalDateTime date = timeStamp.toLocalDateTime();
        item.setCreationDate(date);

        return item;
    }
}
