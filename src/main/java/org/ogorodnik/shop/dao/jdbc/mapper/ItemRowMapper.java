package org.ogorodnik.shop.dao.jdbc.mapper;

import org.ogorodnik.shop.entity.Item;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ItemRowMapper implements RowMapper<Item> {

    public Item mapRow(ResultSet resultSet, int i) throws SQLException {
        return Item.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .price(resultSet.getDouble("price"))
                .creationDate(resultSet.getTimestamp("creationDate").toLocalDateTime())
                .description(resultSet.getString("description"))
                .build();
    }
}
