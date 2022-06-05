package org.ogorodnik.shop.dao.jdbc.mapper;

import org.apache.commons.text.StringEscapeUtils;
import org.ogorodnik.shop.entity.Item;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class ItemRowMapper {

    public Item mapRow(ResultSet resultSet) throws SQLException {
        Item item = new Item();
        item.setId(resultSet.getLong("id"));
        item.setName(StringEscapeUtils.escapeHtml4(resultSet.getString("name")));
        item.setPrice(resultSet.getDouble("price"));

        Timestamp timeStamp = resultSet.getTimestamp("creationDate");
        LocalDateTime date = timeStamp.toLocalDateTime();
        item.setCreationDate(date);

        item.setDescription(resultSet.getString("description") == null ? "" :
                StringEscapeUtils.escapeHtml4(resultSet.getString("description")));

        return item;
    }
}
