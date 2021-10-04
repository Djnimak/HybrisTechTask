package com.tech.database.db.Entity;

public enum OrderStatus {
    CREATED, IN_PROGRESS, DELIVERED;

    public static OrderStatus getStatus (int num) {
        switch (num) {
            case 1: return CREATED;
            default:
            case 2: return IN_PROGRESS;
            case 3: return DELIVERED;
        }
    }
}
