package com.tech.database.db.Entity;

import java.time.LocalDateTime;
import java.util.Objects;

public class Product {

    private int id;
    private String name;
    private int price;
    private LocalDateTime created;
    private ProductStatus status;

    public Product (){
    }

    public Product (String name) {
        this.name = name;
    }

    public static Product createProduct (String name) {
        Product product = new Product(name);
        product.setCreated(LocalDateTime.now());
        product.setStatus(ProductStatus.IN_STOCK);
        return product;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public ProductStatus getStatus() {
        return status;
    }

    public void setStatus(ProductStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return getId() == product.getId() && getName().equals(product.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName());
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", created=" + created +
                ", status=" + status +
                '}';
    }
}
