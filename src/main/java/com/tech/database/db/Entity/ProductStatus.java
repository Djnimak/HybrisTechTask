package com.tech.database.db.Entity;

public enum ProductStatus {
    OUT_OF_STOCK, IN_STOCK, RUNNING_LOW;

    public static ProductStatus getStatus (int num) {
        switch (num) {
            case 1: return OUT_OF_STOCK;
            default:
            case 2: return IN_STOCK;
            case 3: return RUNNING_LOW;
        }
    }
}
