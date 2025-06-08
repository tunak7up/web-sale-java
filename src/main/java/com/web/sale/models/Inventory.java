package com.web.sale.models;

import jakarta.persistence.*;

@Entity
@Table(name = "inventory")
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // Quan hệ nhiều inventory cùng tham chiếu đến một product
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    // Quan hệ nhiều inventory cùng tham chiếu đến một store
    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    private Integer quantity;

    public Inventory() {
    }

    public Inventory(int id, Product product, Store store, Integer quantity) {
        this.id = id;
        this.product = product;
        this.store = store;
        this.quantity = quantity;
    }

    // Getters
    public int getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public Store getStore() {
        return store;
    }

    public Integer getQuantity() {
        return quantity;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Inventory{" +
                "id=" + id +
                ", product=" + (product != null ? product.getId() : null) +
                ", store=" + (store != null ? store.getId() : null) +
                ", quantity=" + quantity +
                '}';
    }
}
