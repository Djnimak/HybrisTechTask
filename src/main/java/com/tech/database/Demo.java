package com.tech.database;

import com.tech.database.db.DBManager;
import com.tech.database.db.Entity.Order;
import com.tech.database.db.Entity.Product;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Demo {

    public static int createOrder(DBManager dbManager, List<Integer> productIDs, List<Integer> productQuantity) throws SQLException, FileNotFoundException {
        Order order = Order.createOrder();
        Map<Integer, Integer> products = new HashMap<>();
        for (int i = 0; i < productIDs.size(); i++) {
            products.merge(productIDs.get(i), productQuantity.get(i), Integer::sum);
        }
        order.setProducts(products);
        return dbManager.createOrder(order, products);
    }

    public static void createProduct(DBManager dbManager, String name, int price) throws SQLException, FileNotFoundException {
        Product product = Product.createProduct(name);
        product.setPrice(price);
        dbManager.createProduct(product);
    }

    public static DBManager createDataBase() {
        DBManager dbManager = DBManager.getInstance();
        try {
            dbManager.executeScript(new FileInputStream("sql/db-create.sql"));
        } catch (SQLException | FileNotFoundException e) {
            e.printStackTrace();
        }
        return dbManager;
    }

    public static void allOrders(DBManager dbManager) throws SQLException, FileNotFoundException {
        Map<Map<Map<Integer, Product>, Map<Integer, Order>>, Map<Integer, Integer>> map = dbManager.findAllOrders();
        System.out.println("| Order ID | Order Created | Product name | Products price | Quantity |");

        List<String> productNames = new LinkedList<>();
        List<Integer> productQuantity = new LinkedList<>();
        Map<Integer, Order> orders = new LinkedHashMap<>();
        List<Order> orderList = new LinkedList<>();
        List<Integer> priceSums = new LinkedList<>();
        List<Integer> sumsTotal = new LinkedList<>();
        List<Integer> orderCount = new LinkedList<>();
        for (Map.Entry<Map<Map<Integer, Product>, Map<Integer, Order>>, Map<Integer, Integer>> mapOrder : map.entrySet()) {
            int count = 1;
            for (Map.Entry<Map<Integer, Product>, Map<Integer, Order>> mapEntry : mapOrder.getKey().entrySet()) {
                for (Map.Entry<Integer, Product> map1 : mapEntry.getKey().entrySet()) {
                    productNames.add(map1.getValue().getName());
                }
                for (Map.Entry<Integer, Order> mapO : mapEntry.getValue().entrySet()) {
                    orderList.add(mapO.getValue());
                    if (orderList.size() > 1) {
                        if (mapO.getValue().getId() == orderList.get(orderList.size() - 2).getId()) {
                            count++;
                        } else {
                            if (mapO.getKey() == productNames.size()) {
                                orderCount.add(count);
                                count = 1;
                                orderCount.add(count);
                                break;
                            } else {
                                orderCount.add(count);
                                count = 1;
                            }
                        }
                    }
                    if (mapO.getKey() == productNames.size()) {
                        orderCount.add(count);
                    }
                }
            }
            int priceSum = 0;
            for (Map.Entry<Integer, Integer> map2 : mapOrder.getValue().entrySet()) {
                productQuantity.add(map2.getValue());
                priceSums.add(map2.getKey());
            }
            for (Integer integer : orderCount) {
                for (int i = 0; i < integer; i++) {
                    priceSum = priceSum + priceSums.get(0);
                    priceSums.remove(0);
                }
                sumsTotal.add(priceSum);
                priceSum = 0;
            }
        }
        int v = 0;
        int g = 0;
        for (Integer integer : orderCount) {

            System.out.println("| " + orderList.get(v).getId() + " | "
                    + orderList.get(v).getCreated().format(DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm"))
                    + " | " + productNames.get(v) + " | " + sumsTotal.get(g)
                    + " | " + productQuantity.get(v) + " |");
            for (int i = 1; i < integer; i++) {
                System.out.print("| ");
                for (int j = 0; j < String.valueOf(orderList.get(integer - 1).getId()).length(); j++) {
                    System.out.print(" ");
                }
                System.out.print(" | ");
                for (int k = 0; k < orderList.get(integer - 1).getCreated().format(DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm")).length(); k++) {
                    System.out.print(" ");
                }
                System.out.print(" | " + productNames.get(i) + " | ");
                for (int o = 0; o < String.valueOf(sumsTotal.get(orderCount.indexOf(integer))).length(); o++) {
                    System.out.print(" ");
                }
                System.out.println(" | " + productQuantity.get(i) + " |");
                v++;
            }
            v++;
            g++;
        }
    }

    public static void allProducts(DBManager dbManager) throws SQLException, FileNotFoundException {
        List<Product> products = dbManager.findAllProducts();
        System.out.println("| Product name | Product price | Product status |");
        for (Product product : products) {
            System.out.println("| " + product.getName() + " | " + product.getPrice() + " | " + product.getStatus() + " |");
        }
    }

    public static void allProductsWithId(DBManager dbManager) throws SQLException, FileNotFoundException {
        List<Product> products = dbManager.allProductsWithID();
        System.out.println("| Product ID | Product name | Product price |");
        for (Product product : products) {
            System.out.println("| " + product.getId() + " | " + product.getName() + " | " + product.getPrice() + " |");
        }
    }

    public static void orderedProducts(DBManager dbManager) throws SQLException, FileNotFoundException {
        Map<Product, Integer> products = dbManager.finaAllOrderedProducts();
        System.out.println("| Product name | Product price | Product status | Total quantity |");
        for (Map.Entry<Product, Integer> map : products.entrySet()) {
            System.out.println("| " + map.getKey().getName() + " | " + map.getKey().getPrice() + " | " + map.getKey().getStatus() + " | " + map.getValue() + " |");
        }
    }

    public static void orderByID(DBManager dbManager, int id) throws SQLException, FileNotFoundException {
        Map<Map<Product, Order>, Map<Integer, Integer>> map = dbManager.findOrderById(id);
        System.out.println("| Order ID | Order Created | Product name | Products price | Quantity |");

        List<String> productNames = new LinkedList<>();
        List<Integer> productQuantity = new LinkedList<>();
        Order order = new Order();
        int priceSum = 0;
        for (Map.Entry<Map<Product, Order>, Map<Integer, Integer>> mapOrder : map.entrySet()) {
            for (Map.Entry<Product, Order> map1 : mapOrder.getKey().entrySet()) {
                productNames.add(map1.getKey().getName());
                order.setId(map1.getValue().getId());
                order.setCreated(map1.getValue().getCreated());
            }
            for (Map.Entry<Integer, Integer> map2 : mapOrder.getValue().entrySet()) {
                productQuantity.add(map2.getValue());
                priceSum = priceSum + map2.getKey();
            }
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm");
        LocalDateTime localDateTime = order.getCreated();
        String created = formatter.format(localDateTime);
        System.out.println("| " + order.getId() + " | "
                + created + " | " + productNames.get(0) + " | " + priceSum + " | " + productQuantity.get(0) + " |");

        for (int i = 1; i < productNames.size(); i++) {
            System.out.print("| ");
            for (int j = 0; j < String.valueOf(order.getId()).length(); j++) {
                System.out.print(" ");
            }
            System.out.print(" | ");
            for (int l = 0; l < order.getCreated().format(DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm")).length(); l++) {
                System.out.print(" ");
            }
            System.out.print(" | " + productNames.get(i) + " | ");
            for (int p = 0; p < String.valueOf(priceSum).length(); p++) {
                System.out.print(" ");
            }
            System.out.println(" | " + productQuantity.get(i) + " |");
        }
    }

    public static void updateProductQuantity (DBManager dbManager, int orderId, int productId, int quantity) throws SQLException, FileNotFoundException {
        dbManager.updateProductQuantityInOrder(quantity, orderId, productId);
    }

    public static void deleteProductByID (DBManager dbManager, int productId) throws SQLException, FileNotFoundException {
        dbManager.deleteProductById(productId);
    }

    public static boolean deleteAllProducts (DBManager dbManager, String password) throws SQLException, FileNotFoundException {
        if (password.equals("qwerty123")) {
            dbManager.deleteProducts();
            return true;
        } else {
            System.out.println("Password is incorrect, please try again.");
            return false;
        }
    }
}
