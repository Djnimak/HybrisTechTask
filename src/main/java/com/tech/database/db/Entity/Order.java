package com.tech.database.db.Entity;

import java.time.LocalDateTime;
import java.util.*;

public class Order {

    private int id;
    private int userId;
    private OrderStatus status;
    private LocalDateTime created;
    private Map<Integer, Integer> products = new HashMap<>();

    public static final Random random = new Random();

    public Order() {
    }

    public static Order createOrder () {
        Order order = new Order();
        order.setStatus(OrderStatus.CREATED);
        order.setCreated(LocalDateTime.now());
        order.setUserId(random.nextInt(100));
        return order;}


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public Map<Integer, Integer> getProducts() {
        return products;
    }

    public void setProducts(Map<Integer , Integer> products) {
        this.products = products;
    }

    @Override
    public boolean equals(Object o) {
        Order order = (Order) o;
        return getId() == order.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUserId());
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", userId=" + userId +
                ", status=" + status +
                ", created=" + created +
                ", products=" + products.toString() +
                '}';
    }
}
