package org.ogorodnik.shop.dao.jdbc;

import org.junit.jupiter.api.Test;
import org.ogorodnik.shop.entity.Item;

import java.io.IOException;
import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JdbcItemDaoITest {

    @Test
    public void testGetAll() throws SQLException, IOException {
        JdbcItemDao jdbcItemDao = new JdbcItemDao();
        List<Item> items = jdbcItemDao.getAll();

        assertFalse(items.isEmpty());
        for (Item item: items){
            assertNotNull(item.getId());
            assertNotNull(item.getName());
            assertNotNull(item.getPrice());
            assertNotNull(item.getCreationDate());
        }
    }

//    @Mock
//    Connection mockConnection;
//
//@InjectMocks
//private JdbcItemDao dbConnection;
//    @Mock private Connection mockConnection;
//    @Mock private Statement mockStatement;
//
//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    public void testMockDBConnection() throws Exception {
//        Mockito.when(mockConnection.createStatement()).thenReturn(mockStatement);
//        Mockito.when(mockConnection.createStatement().executeUpdate(Mockito.any())).thenReturn(1);
//        int value = dbConnection.executeQuery("");
//        Assert.assertEquals(value, 1);
//        Mockito.verify(mockConnection.createStatement(), Mockito.times(1));
//    }
//
//
//    @Test
//    public void testInsertItem() throws SQLException {
//        String insertSql = "INSERT INTO item (name, price, creationdate) values (?, ?, ? ?)";
//
//        JdbcItemDao jdbcItemDao = mock(JdbcItemDao.class);
//
////        when(DriverManager.getConnection(anyString(), anyString(), anyString())).thenReturn(mockConnection);
////        PreparedStatement preparedStatement = mockConnection.prepareStatement(insertSql);
//
//        Item testItem = new Item();
//        testItem.setName("testName");
//        testItem.setPrice(2.2);
//
//        LocalDateTime creationDate =
//                LocalDateTime.of(2010, Month.AUGUST, 10, 9, 15, 20);
//        testItem.setCreationDate(creationDate);
////        jdbcItemDao.insertItem(testItem);
//
////        System.out.println(jdbcItemDao.getInsertSql());
//    }
}