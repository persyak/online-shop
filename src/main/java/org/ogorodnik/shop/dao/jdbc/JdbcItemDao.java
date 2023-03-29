package org.ogorodnik.shop.dao.jdbc;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.ogorodnik.shop.dao.ItemDao;
import org.ogorodnik.shop.dao.jdbc.mapper.ItemRowMapper;
import org.ogorodnik.shop.entity.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
public class JdbcItemDao implements ItemDao {

    private final JdbcTemplate jdbcTemplate;
    private final ItemRowMapper itemRowMapper;
    private final String GET_ALL_SQL = "SELECT id, name, price, creationDate, description FROM item";
    private final String INSERT_SQL =
            "INSERT INTO item (name, price, creationdate, description) values (?, ?, ?,?) " +
                    "RETURNING id, name, price, creationDate, description";
    private final String DELETE_SQL = "DELETE FROM item WHERE id = ?";
    private final String UPDATE_SQL = "UPDATE item SET name=?, price=?, creationDate=?, description=? WHERE id=?";
    private final String SEARCH_ITEM_SQL =
            "SELECT id, name, price, creationDate, description FROM item where name like ? or description like ?";
    private final String GET_ITEM_BY_ID_SQL =
            "SELECT id, name, price, creationDate, description FROM item WHERE id = ?";

    @Autowired
    public JdbcItemDao(JdbcTemplate jdbcTemplate, ItemRowMapper itemRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.itemRowMapper = itemRowMapper;
    }

    @SneakyThrows
    public List<Item> getAll() {
        return jdbcTemplate.query(GET_ALL_SQL, itemRowMapper);
    }

    @SneakyThrows
    public Item addItem(Item item) {
        return jdbcTemplate.queryForObject(INSERT_SQL,
                itemRowMapper,
                item.getName(),
                item.getPrice(),
                java.sql.Timestamp.valueOf(item.getCreationDate()),
                item.getDescription());
    }

    @SneakyThrows
    public int deleteItem(long id) {
        return jdbcTemplate.update(DELETE_SQL, id);
    }

    @SneakyThrows
    public int updateItem(Item item) {
        int updateCount = jdbcTemplate.update(
                UPDATE_SQL,
                item.getName(),
                item.getPrice(),
                java.sql.Timestamp.valueOf(item.getCreationDate()),
                item.getDescription(),
                item.getId());
        if (updateCount > 0) {
            log.info("item {} was update in database", item.getName());
        } else {
            log.info("item {} was not update in database", item.getName());
        }
        return updateCount;
    }

    @SneakyThrows
    public List<Item> search(String searchItem) {
        String searchCriteria = "%" + searchItem + "%";
        return jdbcTemplate.query(SEARCH_ITEM_SQL, itemRowMapper, searchCriteria, searchCriteria);
    }

    @SneakyThrows
    public Item getItemById(long itemId) {
        return jdbcTemplate.queryForObject(GET_ITEM_BY_ID_SQL, itemRowMapper, itemId);
    }
}
