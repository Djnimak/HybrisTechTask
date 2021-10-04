package com.tech.database;

public interface SqlConstance {

    public static final String CREATE_PRODUCT = "INSERT INTO products (id, name, price, created_at, product_status) VALUES(DEFAULT, ?, ?, ?, ?)";
    public static final String CREATE_ORDER = "INSERT INTO orders (id, user_id, order_status, created_at) VALUES (DEFAULT, ?, ?, ?)";
    public static final String ORDER_PRODUCTS = "INSERT INTO orders_has_products (orders_id, products_id, quantity) VALUES (?, ?, ?)";
    public static final String LIST_PRODUCTS = "SELECT name, price, product_status FROM products";
    public static final String LIST_PRODUCTS_WITH_ID = "SELECT id, name, price FROM products";
    public static final String LIST_ORDER_PRODUCT = "SELECT name, price, product_status, sum(quantity) FROM products INNER JOIN orders_has_products on products.id = orders_has_products.products_id GROUP BY name ORDER BY sum(quantity) DESC";
    public static final String ORDER_PRODUCTS_PRICE = "SELECT o.id, sum(quantity * p.price), name, quantity, o.created_at FROM orders_has_products INNER JOIN orders o on orders_has_products.orders_id = o.id INNER JOIN products p on orders_has_products.products_id = p.id WHERE orders_id = ? GROUP BY name";
    public static final String ALL_ORDERS_PRODUCTS_PRICE = "SELECT o.id, sum(quantity * p.price), name, quantity, o.created_at FROM orders_has_products INNER JOIN orders o on orders_has_products.orders_id = o.id INNER JOIN products p on orders_has_products.products_id = p.id GROUP BY o.id, name";
    public static final String DELETE_PRODUCT = "DELETE FROM products WHERE id = ?";
    public static final String DELETE_PRODUCTS = "DELETE FROM products";
    public static final String UPDATE_PRODUCT_QUANTITY_BY_ORDER_ID = "UPDATE orders_has_products SET quantity = ? WHERE orders_id = ? AND products_id = ?";
}
