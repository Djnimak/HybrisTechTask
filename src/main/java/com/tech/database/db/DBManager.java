package com.tech.database.db;

import com.tech.database.SqlConstance;
import com.tech.database.db.Entity.Order;
import com.tech.database.db.Entity.Product;
import com.tech.database.db.Entity.ProductStatus;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.*;

public class DBManager {

    private Connection connection;
    private static DBManager dbManager;
    private DBManager() {}
    public static synchronized DBManager getInstance() {
        if  (dbManager == null) {
            dbManager = new DBManager();
        }
        return dbManager;
    }

    public Connection getConnection() throws SQLException {
        List<String> list = getProperty();
        connection = DriverManager.getConnection(list.get(0), list.get(1), list.get(2));
        return connection;
    }

    public static List<String> getProperty() {
        String connectionURL = "";
        String username = "";
        String password = "";
        try (FileReader reader = new FileReader("local.properties")) {
            Properties properties = new Properties();
            properties.load(reader);
            connectionURL = properties.getProperty("connection.url");
            username = properties.getProperty("username");
            password = properties.getProperty("password");
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<String> list = new ArrayList<>();
        list.add(connectionURL);
        list.add(username);
        list.add(password);
        return list;
    }

    public void createProduct(Product product) throws SQLException {
        ResultSet rs = null;
        connection = getConnection();
        try (PreparedStatement ps = connection.prepareStatement(SqlConstance.CREATE_PRODUCT, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, product.getName());
            ps.setInt(2, product.getPrice());
            ps.setTimestamp(3, Timestamp.valueOf(product.getCreated()));
            ps.setString(4, String.valueOf(product.getStatus()));
            if (ps.executeUpdate() > 0) {
                rs = ps.getGeneratedKeys();
            }
            assert rs != null;
            if (rs.next()) {
                product.setId(rs.getInt(1));
            }
        }   catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(rs);
            connection.close();
        }
    }

    public int createOrder(Order order, Map<Integer, Integer> products) throws SQLException {
        ResultSet rs = null;
        connection = getConnection();
        int orderId = 0;
        try {
            connection.setAutoCommit(false);
            try (PreparedStatement ps1 = connection.prepareStatement(SqlConstance.CREATE_ORDER, Statement.RETURN_GENERATED_KEYS)) {
                ps1.setInt(1, order.getUserId());
                ps1.setString(2, String.valueOf(order.getStatus()));
                ps1.setTimestamp(3, Timestamp.valueOf(order.getCreated()));
                if (ps1.executeUpdate() > 0) {
                    rs = ps1.getGeneratedKeys();
                }
                assert rs != null;
                if (rs.next()) {
                    order.setId(rs.getInt(1));
                }
                connection.commit();
            }
            try (PreparedStatement ps2 = connection.prepareStatement(SqlConstance.ORDER_PRODUCTS)) {
                for (Map.Entry<Integer, Integer> product : products.entrySet()) {
                ps2.setInt(1, order.getId());
                ps2.setInt(2, product.getKey());
                ps2.setInt(3, product.getValue());
                ps2.addBatch();
                }
                ps2.executeBatch();
                connection.commit();
            }
        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            throw new RuntimeException(ex);
        } finally {
            orderId = order.getId();
            close(rs);
            setAutocommit();
            connection.close();
        }
        return orderId;
    }

    public List<Product> findAllProducts() throws SQLException {
        ResultSet rs = null;
        connection = getConnection();
        List<Product> products = new LinkedList<>();
        try (Statement s = connection.createStatement()){
            rs = s.executeQuery(SqlConstance.LIST_PRODUCTS);
            while (rs.next()) {
                Product product = new Product();
                product.setName(rs.getString(1));
                product.setPrice(rs.getInt(2));
                product.setStatus(ProductStatus.valueOf(rs.getString(3).toUpperCase(Locale.ROOT)));
                products.add(product);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        } finally {
            close(rs);
            connection.close();
        }
        return products;
    }

    public List<Product> allProductsWithID() throws SQLException {
        ResultSet rs = null;
        connection = getConnection();
        List<Product> products = new LinkedList<>();
        try (Statement s = connection.createStatement()){
            rs = s.executeQuery(SqlConstance.LIST_PRODUCTS_WITH_ID);
            while (rs.next()) {
                Product product = new Product();
                product.setId(rs.getInt(1));
                product.setName(rs.getString(2));
                product.setPrice(rs.getInt(3));
                products.add(product);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        } finally {
            close(rs);
            connection.close();
        }
        return products;
    }

    public Map<Product, Integer> finaAllOrderedProducts() throws SQLException {
        ResultSet rs = null;
        connection = getConnection();
        Map<Product, Integer> products = new LinkedHashMap<>();
        try (Statement s = connection.createStatement()){
            rs = s.executeQuery(SqlConstance.LIST_ORDER_PRODUCT);
            while (rs.next()) {
                Product product = new Product();
                product.setName(rs.getString(1));
                product.setPrice(rs.getInt(2));
                product.setStatus(ProductStatus.valueOf(rs.getString(3).toUpperCase(Locale.ROOT)));
                products.put(product, rs.getInt(4));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyMap();
        } finally {
            close(rs);
            connection.close();
        }
        return products;
    }

    public Map<Map<Product, Order>, Map<Integer, Integer>> findOrderById(int id) throws SQLException {
        ResultSet rs = null;
        connection = getConnection();
        Map<Map<Product, Order>, Map<Integer, Integer>> map = new LinkedHashMap<>();
        Map<Product, Order> mapOP = new LinkedHashMap<>();
        Map<Integer, Integer> mapSQ = new LinkedHashMap<>();
        try (PreparedStatement ps = connection.prepareStatement(SqlConstance.ORDER_PRODUCTS_PRICE)) {
            ps.setInt(1, id);
            rs = ps.executeQuery();
            while (rs.next()) {
                Order order = new Order();
                Product product = new Product();
                order.setId(id);
                order.setCreated(rs.getTimestamp(5).toLocalDateTime());
                product.setName(rs.getString(3));
                mapOP.put(product, order);
                int quantity = rs.getInt(4);
                int sum = rs.getInt(2);
                mapSQ.put(sum, quantity);
            }
            map.put(mapOP, mapSQ);
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyMap();
        } finally {
            close(rs);
            connection.close();
        }
        return map;
    }

    public Map<Map<Map<Integer,Product>, Map<Integer,Order>>, Map<Integer, Integer>> findAllOrders() throws SQLException {
        ResultSet rs = null;
        connection = getConnection();
        Map<Map<Map<Integer,Product>, Map<Integer,Order>>, Map<Integer, Integer>> map = new LinkedHashMap<>();
        Map<Map<Integer,Product>, Map<Integer,Order>> mapOP = new LinkedHashMap<>();
        Map<Integer, Product> mapProduct = new LinkedHashMap<>();
        Map<Integer, Order> mapOrder = new LinkedHashMap<>();
        Map<Integer, Integer> mapSQ = new LinkedHashMap<>();
        try (PreparedStatement ps = connection.prepareStatement(SqlConstance.ALL_ORDERS_PRODUCTS_PRICE)) {
            rs = ps.executeQuery();
            int count = 1;
            while (rs.next()) {
                Order order = new Order();
                Product product = new Product();
                order.setId(rs.getInt(1));
                order.setCreated(rs.getTimestamp(5).toLocalDateTime());
                product.setName(rs.getString(3));
                mapOrder.put(count, order);
                mapProduct.put(count, product);
                count++;
                int quantity = rs.getInt(4);
                int sum = rs.getInt(2);
                mapSQ.put(sum, quantity);
            }
            mapOP.put(mapProduct, mapOrder);
            map.put(mapOP, mapSQ);
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyMap();
        } finally {
            close(rs);
            connection.close();
        }
        return map;
    }

    public void updateProductQuantityInOrder(int quantity, int orderId, int productId) throws SQLException {
        connection = getConnection();
        try (PreparedStatement ps = connection.prepareStatement(SqlConstance.UPDATE_PRODUCT_QUANTITY_BY_ORDER_ID)) {
            ps.setInt(1, quantity);
            ps.setInt(2, orderId);
            ps.setInt(3, productId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
    }

    public void deleteProductById (int id) throws SQLException {
        connection = getConnection();
        try (PreparedStatement ps = connection.prepareStatement(SqlConstance.DELETE_PRODUCT)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
    }

    public void deleteProducts () throws SQLException {
        connection = getConnection();
        try (PreparedStatement ps = connection.prepareStatement(SqlConstance.DELETE_PRODUCTS)){
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
    }

    private static void close(Statement st) {
        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static void close(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void setAutocommit() {
        try {
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void executeScript(InputStream in) throws SQLException {
        connection = getConnection();
        Scanner s = new Scanner(in, "UTF-8");
        s.useDelimiter("/\\*[\\s\\S]*?\\*/|--[^\\r\\n]*|;");
        Statement st = null;
        try {
            st = connection.createStatement();
            while (s.hasNext()) {
                String line = s.next().trim();
                if (!line.isEmpty()) {
                    st.execute(line);
                }
            }
        } finally {
            close(st);
        }
    }

    public Product getProductByName(String name) throws SQLException {
        ResultSet rs = null;
        Product product = null;
        connection = getConnection();
        try (PreparedStatement ps = connection.prepareStatement(SqlConstance.PRODUCT_BY_NAME)){
            ps.setString(1, name);
            rs = ps.executeQuery();
            if (rs.next()) {
                product = new Product();
                product.setId(rs.getInt("id"));
                product.setName(rs.getString("name"));
                product.setPrice(rs.getInt("price"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(rs);
            connection.close();
        }
        return product;
    }

}
