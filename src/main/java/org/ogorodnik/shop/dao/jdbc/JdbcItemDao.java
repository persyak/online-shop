package org.ogorodnik.shop.dao.jdbc;

import org.ogorodnik.shop.dao.ItemDao;
import org.ogorodnik.shop.dao.jdbc.mapper.ItemRowMapper;
import org.ogorodnik.shop.entity.Item;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class JdbcItemDao implements ItemDao {
    private final String GET_ALL_SQL = "SELECT id, name, price, creationDate, description FROM item";
    private final String insertSql = "INSERT INTO item (name, price, creationdate, description) values (?, ?, ?,?)";
    private final String DELETE_SQL = "DELETE FROM item WHERE id = ?";
    private final String UPDATE_SQL = "UPDATE item SET name=?, price=?, creationDate=?, description=? WHERE id=?";
    private final String SEARCHITEM_SQL =
            "SELECT id, name, price, creationDate, description FROM item where name like ? or description like ?";

    private final DataSource dataSource;

    public JdbcItemDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Item> getAll() throws SQLException {
        List<Item> items = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(GET_ALL_SQL)) {
            ItemRowMapper itemRowMapper = new ItemRowMapper();

            while (resultSet.next()) {
                Item item = itemRowMapper.mapRow(resultSet);
                items.add(item);
            }
        }
        return items;
    }

    public void insertItem(Item item) throws SQLException {
        String name = item.getName();
        double price = item.getPrice();
        LocalDateTime creationDate = item.getCreationDate();
        String description = item.getDescription();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement insertPreparedSql = connection.prepareStatement(insertSql);
        ) {
            insertPreparedSql.setString(1, name);
            insertPreparedSql.setDouble(2, price);

            Timestamp timestamp = java.sql.Timestamp.valueOf(creationDate);
            insertPreparedSql.setTimestamp(3, timestamp);

            insertPreparedSql.setString(4, description);

            insertPreparedSql.executeUpdate();
        }
    }

    public void deleteItem(long id) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement deletePreparedSql = connection.prepareStatement(DELETE_SQL)) {
            deletePreparedSql.setLong(1, id);

            deletePreparedSql.execute();
        }
    }

    public void updateItem(Item item, long id) throws SQLException {
        String name = item.getName();
        double price = item.getPrice();
        LocalDateTime creationDate = item.getCreationDate();
        String description = item.getDescription();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement updatePreparedSql = connection.prepareStatement(UPDATE_SQL)) {
            updatePreparedSql.setString(1, name);
            updatePreparedSql.setDouble(2, price);

            Timestamp timestamp = java.sql.Timestamp.valueOf(creationDate);
            updatePreparedSql.setTimestamp(3, timestamp);
            updatePreparedSql.setString(4, description);
            updatePreparedSql.setLong(5, id);

            updatePreparedSql.executeUpdate();
        }
    }

    @Override
    public List<Item> search(String searchItem) throws SQLException {
        List<Item> items = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement searchPreparedSql = connection.prepareStatement(SEARCHITEM_SQL)) {
            String searchCriteria = "%" + searchItem + "%";
            searchPreparedSql.setString(1, searchCriteria);
            searchPreparedSql.setString(2, searchCriteria);
            ResultSet resultSet = searchPreparedSql.executeQuery();
                ItemRowMapper itemRowMapper = new ItemRowMapper();

                while (resultSet.next()) {
                    Item item = itemRowMapper.mapRow(resultSet);
                    items.add(item);
                }
            }
        return items;
    }
}
