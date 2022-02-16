package org.ogorodnik.shop.dao.jdbc;

import org.ogorodnik.shop.dao.jdbc.mapper.ItemRowMapper;
import org.ogorodnik.shop.entity.Item;
import org.ogorodnik.shop.utility.PropertyHandler;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class JdbcItemDao {
    private final String GET_ALL_SQL = "SELECT id, name, price, creationDate FROM item;";
    private final String insertSql = "INSERT INTO item (name, price, creationdate) values (?, ?, ?)";
    private final String DELETE_SQL = "DELETE FROM item WHERE id = ?";
    private final String UPDATE_SQL = "UPDATE item SET name=?, price=?, creationDate=? WHERE id=?";
    private String databaseConfiguration = "configurations/databaseConfiguration.properties";

    public List<Item> getAll() throws SQLException, IOException {
        List<Item> items = new ArrayList<>();
        try(Connection connection = getConnection();
            Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(GET_ALL_SQL);

            ItemRowMapper itemRowMapper = new ItemRowMapper();

            while(resultSet.next()){
                Item item = itemRowMapper.mapRow(resultSet);
                items.add(item);
            }
        }
        return items;
    }

    public void insertItem(Item item) throws SQLException, IOException {
        String name = item.getName();
        double price = item.getPrice();
        LocalDateTime creationDate = item.getCreationDate();

        try(Connection connection = getConnection();
            PreparedStatement insertPreparedSql = connection.prepareStatement(insertSql)) {
            insertPreparedSql.setString(1, name);
            insertPreparedSql.setDouble(2, price);

            Timestamp timestamp = java.sql.Timestamp.valueOf(creationDate);
            insertPreparedSql.setTimestamp(3, timestamp);

            insertPreparedSql.executeUpdate();
        }
    }

    public void deleteItem(long id) throws SQLException, IOException {
        try(Connection connection = getConnection();
            PreparedStatement deletePreparedSql = connection.prepareStatement(DELETE_SQL)){
            deletePreparedSql.setLong(1, id);

            deletePreparedSql.execute();
        }
    }

    public void updateItem(Item item, long id) throws SQLException, IOException {
        String name = item.getName();
        double price = item.getPrice();
        LocalDateTime creationDate = item.getCreationDate();

        try(Connection connection = getConnection();
            PreparedStatement updatePreparedSql = connection.prepareStatement(UPDATE_SQL)) {
            updatePreparedSql.setString(1, name);
            updatePreparedSql.setDouble(2, price);

            Timestamp timestamp = java.sql.Timestamp.valueOf(creationDate);
            updatePreparedSql.setTimestamp(3, timestamp);

            updatePreparedSql.setLong(4, id);

            updatePreparedSql.executeUpdate();
        }
    }

    private Connection getConnection() throws SQLException, IOException {
        Properties dbConnectionProperty = PropertyHandler.getConfigPropery(databaseConfiguration);
        String url = dbConnectionProperty.getProperty("url");
        String user = dbConnectionProperty.getProperty("user");
        String password = dbConnectionProperty.getProperty("password");

        return DriverManager.getConnection(url, user, password);
    }
}
