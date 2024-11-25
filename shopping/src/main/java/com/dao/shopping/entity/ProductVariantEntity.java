package com.dao.shopping.entity;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;


@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "variant")
@AllArgsConstructor
@NoArgsConstructor
public class ProductVariantEntity extends BaseEntity{

    @ManyToOne
    @JoinColumn(name = "product_id")
    ProductEntity product;

    @ManyToOne
    @JoinColumn(name = "color_id")
    ColorEntity color;

    @ManyToOne
    @JoinColumn(name = "size_id")
    SizeEntity size;

    Integer quantity;

    @OneToMany(mappedBy = "variant", cascade = CascadeType.ALL)
    List<OrderItemEntity> orderItems;

    @OneToMany(mappedBy = "variant", cascade = CascadeType.ALL)
    List<CartItemEntity> cartItems;

    @Column(name = "is_sold_out", nullable = false)
    boolean isSoldOut;

    @Column(name = "sold_quantity")
    Integer soldQuantity;

    @Column(name = "is_available", nullable = false)
    boolean isAvailable = true;

    public ProductEntity getProduct() {
        return product;
    }

    public void setProduct(ProductEntity product) {
        this.product = product;
    }

    public ColorEntity getColor() {
        return color;
    }

    public void setColor(ColorEntity color) {
        this.color = color;
    }

    public SizeEntity getSize() {
        return size;
    }

    public void setSize(SizeEntity size) {
        this.size = size;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public List<OrderItemEntity> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemEntity> orderItems) {
        this.orderItems = orderItems;
    }

    public List<CartItemEntity> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItemEntity> cartItems) {
        this.cartItems = cartItems;
    }

    public boolean isSoldOut() {
        return isSoldOut;
    }

    public void setSoldOut(boolean soldOut) {
        isSoldOut = soldOut;
    }

    public Integer getSoldQuantity() {
        return soldQuantity;
    }

    public void setSoldQuantity(Integer soldQuantity) {
        this.soldQuantity = soldQuantity;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}
