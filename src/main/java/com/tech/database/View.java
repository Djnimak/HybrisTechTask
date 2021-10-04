package com.tech.database;

import com.tech.database.db.DBManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class View {

    public static void main(String[] args) throws IOException {
        DBManager dbManager = Demo.createDataBase();
        System.out.print(ViewConstance.MAIN_MENU);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            boolean end = true;
            while (end) {
                try {
                    int command;
                    while ((command = Integer.parseInt(reader.readLine())) != 10) {
                        String info;
                        int info2;
                        List<Integer> info3 = new LinkedList<>();
                        List<Integer> info4 = new LinkedList<>();
                        switch (command) {
                            case 1:
                                System.out.print(ViewConstance.CREATE_PRODUCT_NAME);
                                info = reader.readLine();
                                System.out.print(ViewConstance.CREATE_PRODUCT_PRICE);
                                info2 = Integer.parseInt(reader.readLine());
                                Demo.createProduct(dbManager, info, info2);
                                System.out.println(ViewConstance.PRODUCT_CREATED);
                                System.out.println(ViewConstance.MAIN_MENU);
                                break;
                            case 2:
                                Demo.allProductsWithId(dbManager);
                                System.out.print(ViewConstance.CREATE_ORDER);
                                while (!(info = reader.readLine()).equalsIgnoreCase(ViewConstance.STOP)) {
                                    System.out.print(ViewConstance.CREATE_ORDER_QUANTITY);
                                    info3.add(Integer.parseInt(info));
                                    info4.add(Integer.parseInt(reader.readLine()));
                                    System.out.println(ViewConstance.TYPE_PLACE);
                                    System.out.print(ViewConstance.CREATE_ORDER);
                                }
                                int orderID = Demo.createOrder(dbManager, info3, info4);
                                System.out.println(ViewConstance.ORDER_CREATED + orderID);
                                System.out.println(ViewConstance.MAIN_MENU);
                                break;
                            case 3:
                                System.out.print(ViewConstance.ORDER_ID);
                                int one = Integer.parseInt(reader.readLine());
                                System.out.print(ViewConstance.PRODUCT_ID);
                                int two = Integer.parseInt(reader.readLine());
                                System.out.print(ViewConstance.UPDATE_QUANTITY);
                                int three = Integer.parseInt(reader.readLine());
                                Demo.updateProductQuantity(dbManager, one, two, three);
                                System.out.println(ViewConstance.QUANTITY_UPDATED);
                                System.out.println(ViewConstance.MAIN_MENU);
                                break;
                            case 4:
                                Demo.allProducts(dbManager);
                                System.out.println(ViewConstance.MAIN_MENU);
                                break;
                            case 5:
                                Demo.orderedProducts(dbManager);
                                System.out.println(ViewConstance.MAIN_MENU);
                                break;
                            case 6:
                                System.out.print(ViewConstance.ORDER_ID);
                                info2 = Integer.parseInt(reader.readLine());
                                Demo.orderByID(dbManager, info2);
                                System.out.println(ViewConstance.MAIN_MENU);
                                break;
                            case 7:
                                Demo.allOrders(dbManager);
                                System.out.println(ViewConstance.MAIN_MENU);
                                break;
                            case 8:
                                System.out.print(ViewConstance.PRODUCT_ID);
                                info2 = Integer.parseInt(reader.readLine());
                                Demo.deleteProductByID(dbManager, info2);
                                System.out.println(ViewConstance.PRODUCT_DELETED);
                                System.out.println(ViewConstance.MAIN_MENU);
                                break;
                            case 9:
                                System.out.print(ViewConstance.PASSWORD_REQUEST);
                                info = reader.readLine();
                                while (!Demo.deleteAllProducts(dbManager, info)) {
                                    info = reader.readLine();
                                }
                                System.out.println(ViewConstance.ALL_PRODUCTS_DELETED);
                                System.out.println(ViewConstance.MAIN_MENU);
                                break;
                            default:
                                System.out.print(ViewConstance.INCORRECT_COMMAND_NUMBER);
                        }
                    }
                    end = false;
                } catch (NumberFormatException | SQLException e) {
                    System.out.println(ViewConstance.INCORRECT_COMMAND_NUMBER);
                }
            }
        }
    }
}
