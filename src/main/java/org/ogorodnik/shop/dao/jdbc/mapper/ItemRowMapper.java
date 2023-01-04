package org.ogorodnik.shop.dao.jdbc.mapper;

import org.apache.commons.text.StringEscapeUtils;
import org.ogorodnik.shop.entity.Item;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ItemRowMapper {

    public Item mapRow(ResultSet resultSet) throws SQLException {
        return Item.builder()
                .id(resultSet.getLong("id"))
                .name(StringEscapeUtils.escapeHtml4(resultSet.getString("name")))
                .price(resultSet.getDouble("price"))
                .creationDate(resultSet.getTimestamp("creationDate").toLocalDateTime())
                .description(resultSet.getString("description") == null ? "" :
                        StringEscapeUtils.escapeHtml4(resultSet.getString("description")))
                .build();
    }
}
