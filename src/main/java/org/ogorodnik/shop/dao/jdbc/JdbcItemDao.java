package org.ogorodnik.shop.dao.jdbc;

import lombok.extern.slf4j.Slf4j;
import org.ogorodnik.shop.dao.ItemDao;
import org.ogorodnik.shop.dao.jdbc.mapper.ItemRowMapper;
import org.ogorodnik.shop.entity.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.ogorodnik.shop.dao.jdbc.util.JdbcUtil.handleSqlException;

@Slf4j
@Component
public class JdbcItemDao implements ItemDao {
    private final String GET_ALL_SQL = "SELECT id, name, price, creationDate, description FROM item";
    private final String INSERT_SQL = "INSERT INTO item (name, price, creationdate, description) values (?, ?, ?,?)";
    private final String DELETE_SQL = "DELETE FROM item WHERE id = ?";
    private final String UPDATE_SQL = "UPDATE item SET name=?, price=?, creationDate=?, description=? WHERE id=?";
    private final String SEARCHITEM_SQL =
            "SELECT id, name, price, creationDate, description FROM item where name like ? or description like ?";
    private final String GET_ITEM_BY_ID_SQL =
            "SELECT id, name, price, creationDate, description FROM item WHERE id = ?";

    private final DataSource dataSource;

    @Autowired
    public JdbcItemDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Item> getAll() {
        List<Item> items = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(GET_ALL_SQL)) {
            ItemRowMapper itemRowMapper = new ItemRowMapper();

            while (resultSet.next()) {
                Item item = itemRowMapper.mapRow(resultSet);
                items.add(item);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return items;
    }

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
        } catch (SQLException throwable) {
            handleSqlException(throwable);
        }
    }

    public void deleteItem(long id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement deletePreparedSql = connection.prepareStatement(DELETE_SQL)) {
            deletePreparedSql.setLong(1, id);

            deletePreparedSql.execute();
        } catch (SQLException throwable) {
            handleSqlException(throwable);
        }
    }

    public void updateItem(Item item, long id) {
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
        } catch (SQLException throwable) {
            handleSqlException(throwable);
        }
    }

    @Override
    public List<Item> search(String searchItem) {
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
        } catch (SQLException throwable) {
            handleSqlException(throwable);
        }
        return items;
    }

    @Override
    public Item getItemById(long itemId) {
        Item item = null;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement getItemByIdSql = connection.prepareStatement(GET_ITEM_BY_ID_SQL)) {
            getItemByIdSql.setLong(1, itemId);
            ResultSet resultSet = getItemByIdSql.executeQuery();
            ItemRowMapper itemRowMapper = new ItemRowMapper();
            while (resultSet.next()) {
                item = itemRowMapper.mapRow(resultSet);
            }
        } catch (SQLException throwable) {
            handleSqlException(throwable);
        }
        return item;
    }
}
