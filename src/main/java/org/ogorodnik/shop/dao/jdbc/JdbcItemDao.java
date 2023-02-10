package org.ogorodnik.shop.dao.jdbc;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.ogorodnik.shop.dao.ItemDao;
import org.ogorodnik.shop.dao.jdbc.mapper.ItemRowMapper;
import org.ogorodnik.shop.entity.Item;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
public class JdbcItemDao implements ItemDao {
    private final String GET_ALL_SQL = "SELECT id, name, price, creationDate, description FROM item";
    private final String INSERT_SQL = "INSERT INTO item (name, price, creationdate, description) values (?, ?, ?,?)";
    private final String DELETE_SQL = "DELETE FROM item WHERE id = ?";
    private final String UPDATE_SQL = "UPDATE item SET name=?, price=?, creationDate=?, description=? WHERE id=?";
    private final String SEARCH_ITEM_SQL =
            "SELECT id, name, price, creationDate, description FROM item where name like ? or description like ?";
    private final String GET_ITEM_BY_ID_SQL =
            "SELECT id, name, price, creationDate, description FROM item WHERE id = ?";
    private final static ItemRowMapper itemRowMapper = new ItemRowMapper();

    private final DataSource dataSource;

    public JdbcItemDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @SneakyThrows
    public List<Item> getAll() {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(GET_ALL_SQL)) {

            List<Item> items = new ArrayList<>();
            while (resultSet.next()) {
                Item item = itemRowMapper.mapRow(resultSet);
                items.add(item);
            }
            return items;
        }
    }

    @SneakyThrows
    public void addItem(Item item) {
        String name = item.getName();
        double price = item.getPrice();
        LocalDateTime creationDate = item.getCreationDate();
        String description = item.getDescription();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement insertPreparedSql = connection.prepareStatement(INSERT_SQL)) {
            insertPreparedSql.setString(1, name);
            insertPreparedSql.setDouble(2, price);

            Timestamp timestamp = java.sql.Timestamp.valueOf(creationDate);
            insertPreparedSql.setTimestamp(3, timestamp);

            insertPreparedSql.setString(4, description);

            insertPreparedSql.executeUpdate();
        }
    }

    @SneakyThrows
    public void deleteItem(long id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement deletePreparedSql = connection.prepareStatement(DELETE_SQL)) {
            deletePreparedSql.setLong(1, id);

            deletePreparedSql.execute();
        }
    }

    @SneakyThrows
    public int updateItem(Item item) {
        long id = item.getId();
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

            int updateItemCount = updatePreparedSql.executeUpdate();
            if (updateItemCount > 0) {
                log.info("item {} was update in database", item.getName());
            } else {
                log.info("item {} was not update in database", item.getName());
            }
            return updateItemCount;
        }
    }

    @SneakyThrows
    public List<Item> search(String searchItem) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement searchPreparedSql = connection.prepareStatement(SEARCH_ITEM_SQL)) {

            List<Item> items = new ArrayList<>();
            String searchCriteria = "%" + searchItem + "%";
            searchPreparedSql.setString(1, searchCriteria);
            searchPreparedSql.setString(2, searchCriteria);
            try (ResultSet resultSet = searchPreparedSql.executeQuery()) {
                while (resultSet.next()) {
                    Item item = itemRowMapper.mapRow(resultSet);
                    items.add(item);
                }
                return items;
            }
        }
    }

    @SneakyThrows
    public Item getItemById(long itemId) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement getItemByIdSql = connection.prepareStatement(GET_ITEM_BY_ID_SQL)) {
            Item item = null;
            getItemByIdSql.setLong(1, itemId);
            try (ResultSet resultSet = getItemByIdSql.executeQuery()) {
                while (resultSet.next()) {
                    item = itemRowMapper.mapRow(resultSet);
                }
                return item;
            }
        }
    }
}
