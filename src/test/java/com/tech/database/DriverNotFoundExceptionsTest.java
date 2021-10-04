package com.tech.database;

import com.tech.database.db.DBManager;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Spy;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DriverNotFoundExceptionsTest {

    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
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

    @BeforeClass
    public static void beforeTest() throws ClassNotFoundException {
        Class.forName(JDBC_DRIVER);

        try (OutputStream output = new FileOutputStream("local.properties")) {
            Properties prop = new Properties();
            prop.setProperty(PROPERTIES_KEY, URL_CONNECTION);
            prop.setProperty(PROPERTIES_KEY_USER, USER);
            prop.setProperty(PROPERTIES_KEY_PASS, PASS);
            prop.store(output, null);
        } catch (IOException io) {
            io.printStackTrace();
        }
        dbManager = DBManager.getInstance();
    }

    @Before
    public void beforeEachTest() throws SQLException{
        try (Connection con = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement statement = con.createStatement()) {
            String sql = "DROP TABLE if exists orders;" + System.lineSeparator() +
                    "DROP TABLE if exists products;" + System.lineSeparator() +
                    "DROP TABLE if exists orders_has_products;" + System.lineSeparator() +
                    "CREATE TABLE IF NOT EXISTS `orders` (" + System.lineSeparator() +
                    "   `id` INT NOT NULL AUTO_INCREMENT," + System.lineSeparator() +
                    "   `user_id` INT NOT NULL," + System.lineSeparator() +
                    "   `order_status` ENUM('created', 'in_progress', 'delivered') NOT NULL," + System.lineSeparator() +
                    "   `created_at` VARCHAR(45) NOT NULL," + System.lineSeparator() +
                    "   PRIMARY KEY (`id`)," + System.lineSeparator() +
                    "   UNIQUE INDEX `user_id_UNIQUE` (`user_id` ASC) VISIBLE);" + System.lineSeparator() +
                    "CREATE TABLE IF NOT EXISTS `products` (" + System.lineSeparator() +
                    "   `id` INT NOT NULL AUTO_INCREMENT," + System.lineSeparator() +
                    "   `name` VARCHAR(255) NOT NULL," + System.lineSeparator() +
                    "   `price` INT NOT NULL," + System.lineSeparator() +
                    "   `created_at` DATETIME NOT NULL," + System.lineSeparator() +
                    "   `product_status` ENUM('out_of_stock', 'in_stock', 'running_low') NOT NULL," + System.lineSeparator() +
                    "   PRIMARY KEY (`id`));" + System.lineSeparator() +
                    "CREATE TABLE IF NOT EXISTS `orders_has_products` (" + System.lineSeparator() +
                    "   `orders_id` INT NOT NULL," + System.lineSeparator() +
                    "   `products_id` INT NOT NULL," + System.lineSeparator() +
                    "   `quantity` INT NOT NULL," + System.lineSeparator() +
                    "   PRIMARY KEY (`orders_id`, `products_id`)," + System.lineSeparator() +
                    "   INDEX `fk_orders_has_products_products1_idx` (`products_id` ASC) VISIBLE," + System.lineSeparator() +
                    "   INDEX `fk_orders_has_products_orders1_idx` (`orders_id` ASC) VISIBLE," + System.lineSeparator() +
                    "   CONSTRAINT `fk_orders_has_products_orders1`" + System.lineSeparator() +
                    "   FOREIGN KEY (`orders_id`)" + System.lineSeparator() +
                    "   REFERENCES `orders` (`id`)" + System.lineSeparator() +
                    "   ON DELETE CASCADE" + System.lineSeparator() +
                    "   ON UPDATE CASCADE," + System.lineSeparator() +
                    "   CONSTRAINT `fk_orders_has_products_products1`" + System.lineSeparator() +
                    "   FOREIGN KEY (`products_id`)" + System.lineSeparator() +
                    "   REFERENCES `products` (`id`)" + System.lineSeparator() +
                    "   ON DELETE CASCADE" + System.lineSeparator() +
                    "   ON UPDATE CASCADE);" + System.lineSeparator() +
                    "INSERT INTO products (id, name, price, created_at, product_status) VALUES(DEFAULT, 'Mouse', 44, now(), 'in_stock');" + System.lineSeparator() +
                    "INSERT INTO products (id, name, price, created_at, product_status) VALUES(DEFAULT, 'Monitor', 2000, now(), 'in_stock');" + System.lineSeparator() +
                    "INSERT INTO products (id, name, price, created_at, product_status) VALUES(DEFAULT, 'Keyboard', 254, now(), 'in_stock');" + System.lineSeparator() +
                    "INSERT INTO products (id, name, price, created_at, product_status) VALUES(DEFAULT, 'Mousepad', 100, now(), 'in_stock');" + System.lineSeparator() +
                    "INSERT INTO products (id, name, price, created_at, product_status) VALUES(DEFAULT, 'Speaker', 500, now(), 'in_stock');" + System.lineSeparator() +
                    "INSERT INTO products (id, name, price, created_at, product_status) VALUES(DEFAULT, 'Cooler', 678, now(), 'in_stock');" + System.lineSeparator() +
                    "INSERT INTO products (id, name, price, created_at, product_status) VALUES(DEFAULT, 'Processor', 3000, now(), 'in_stock');" + System.lineSeparator() +
                    "INSERT INTO products (id, name, price, created_at, product_status) VALUES(DEFAULT, 'Videocard', 2354, now(), 'in_stock');" + System.lineSeparator() +
                    "INSERT INTO products (id, name, price, created_at, product_status) VALUES(DEFAULT, 'Cables', 40, now(), 'in_stock');" + System.lineSeparator() +
                    "INSERT INTO products (id, name, price, created_at, product_status) VALUES(DEFAULT, 'HDD', 690, now(), 'in_stock');" + System.lineSeparator();

            statement.executeUpdate(sql);
        }
    }

    @AfterClass
    public static void afterTest() {
        try (OutputStream output = new FileOutputStream("local.properties")) {
            Properties prop = new Properties();
            prop.setProperty(PROPERTIES_KEY, PROPERTIES_VALUE);
            prop.setProperty(PROPERTIES_KEY_USER, PROPERTIES_VALUE_USER);
            prop.setProperty(PROPERTIES_KEY_PASS, PROPERTIES_VALUE_PASS);
            prop.store(output, null);
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    @Test (expected = SQLException.class)
    public void demoExceptionTest() throws SQLException, IOException {
        View.main(null);
    }


}
