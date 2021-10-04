package com.tech.database;

public interface ViewConstance {
    public static final String MAIN_MENU = "1. Create Product"+System.lineSeparator() +
            "2. Create order" + System.lineSeparator() +
            "3. Update product quantity" + System.lineSeparator() +
            "4. Show all products" + System.lineSeparator() +
            "5. Show all ordered products" + System.lineSeparator() +
            "6. Show order by ID" + System.lineSeparator() +
            "7. Show all orders" + System.lineSeparator() +
            "8. Remove product by ID" + System.lineSeparator() +
            "9. Remove all products" + System.lineSeparator() +
            "10. End" + System.lineSeparator() +
            "Enter command number: ";

    public static final String CREATE_PRODUCT_NAME = "Enter product name: ";
    public static final String CREATE_PRODUCT_PRICE = System.lineSeparator() + "Enter product price: ";
    public static final String PRODUCT_CREATED = "Product was successfully create!";

    public static final String CREATE_ORDER = "Please enter Product ID you would like to order: ";
    public static final String CREATE_ORDER_QUANTITY = "Please enter quantity of for the chosen product: ";
    public static final String STOP = "Place";
    public static final String TYPE_PLACE = "Type Place to place an order";
    public static final String ORDER_CREATED = "Order was successfully placed. Your OrderID is ";

    public static final String ORDER_ID = "Please enter order ID ";
    public static final String PRODUCT_ID = "Please enter product ID ";
    public static final String UPDATE_QUANTITY = "Please enter new quantity ";
    public static final String QUANTITY_UPDATED = "Product quantity was successfully updated!";

    public static final String PRODUCT_DELETED = "Product was successfully deleted!";
    public static final String ALL_PRODUCTS_DELETED = "All products have been deleted";

    public static final String PASSWORD_REQUEST = "Please enter a password ";

    public static final String INCORRECT_COMMAND_NUMBER = "Incorrect command number. Please try again"
            + System.lineSeparator() + "Enter command number: ";



}
