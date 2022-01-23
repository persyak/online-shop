package org.ogorodnik.shop.dao.jdbc.mapper;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.ogorodnik.shop.entity.Item;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.Month;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemRowMapperTest {

    @Test
    public void testMapRow() throws Exception {
        ItemRowMapper itemRowMapper = new ItemRowMapper();

        ResultSet mockResultSet = mock(ResultSet.class);

        when(mockResultSet.getLong("id")).thenReturn(1L);
        when(mockResultSet.getString("name")).thenReturn("test_name");
        when(mockResultSet.getDouble("price")).thenReturn(10.6);

        LocalDateTime creationDate =
                LocalDateTime.of(2010, Month.AUGUST, 10, 9, 15, 20);
        Timestamp timestamp = Timestamp.valueOf(creationDate);
        when(mockResultSet.getTimestamp("creationDate")).thenReturn(timestamp);

        Item actual = itemRowMapper.mapRow(mockResultSet);

        assertNotNull(actual);
        assertEquals(1, actual.getId());
        assertEquals("test_name", actual.getName());
        assertEquals(10.6, actual.getPrice());
        assertEquals(creationDate, actual.getCreationDate());
    }
}