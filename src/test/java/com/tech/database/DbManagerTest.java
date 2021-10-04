package com.tech.database;

import com.tech.database.db.DBManager;
import com.tech.database.db.Entity.Product;
import org.junit.*;
import org.mockito.Spy;

import java.io.*;
import java.sql.*;
import java.util.Properties;

public class DbManagerTest {

    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/Test";
    private static final String URL_CONNECTION = "jdbc:mysql://localhost:3306/Test";
    private static final String USER = "root";
    private static final String PASS = "root";

    private static final String PROPERTIES_KEY = "connection.url";
    private static final String PROPERTIES_VALUE = "jdbc:mysql://localhost:3306/HybrisTechTask";
    private static final String PROPERTIES_KEY_USER = "username";
    private static final String PROPERTIES_VALUE_USER = "root";
    private static final String PROPERTIES_KEY_PASS = "password";
    private static final String PROPERTIES_VALUE_PASS = "root";

    @Spy
    private static DBManager dbManager;


    @Before
    public void beforeEachTest() throws SQLException, FileNotFoundException {
        dbManager = Demo.createDataBase();

    }


    @Test
    public void createProductTest() throws SQLException, FileNotFoundException {
        Product product = Product.createProduct("Cup");
        product.setPrice(10);
        dbManager.createProduct(product);
        Product product1 = dbManager.getProductByName(product.getName());
        Assert.assertEquals(product.getName(), product1.getName());
        dbManager.deleteProductById(product1.getId());

    }

}
