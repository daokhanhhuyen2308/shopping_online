package com.dao.shopping.entity;

import com.dao.shopping.enums.OrderStatusType;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "tbl_order")
public class OrderEntity extends BaseEntity{

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    UserEntity user;

    @Enumerated(EnumType.STRING)
    OrderStatusType orderStatus = OrderStatusType.Pending;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "update_status_date")
    Date updateStatusDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "delivery_date")
    Date deliveryTime;

    @ToString.Exclude
    @JsonManagedReference
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    List<OrderItemEntity> orderItems;

    Integer totalQuantity;

    @Column(precision = 9, scale = 2)
    BigDecimal totalPrice;

    //Default shipping cost is 50k
    @Column(precision = 9, scale = 2)
    BigDecimal shippingCost = BigDecimal.valueOf(50000);

    BigDecimal discountPrice = BigDecimal.ZERO;

    @Column(precision = 9, scale = 2)
    BigDecimal finalPrice;

    String appliedVoucher;

    @ManyToOne
    @JoinColumn(name = "voucher_id", referencedColumnName = "id")
    VoucherEntity voucher;

    @ManyToOne
    @JoinColumn(name = "recipient_id")
    RecipientEntity recipient;

    @ManyToOne
    @JoinColumn(name = "address_id")
    AddressEntity address;

    @ManyToOne
    @JoinColumn(name = "payment_id", referencedColumnName = "id")
    PaymentEntity payment;

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public OrderStatusType getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatusType orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Date getUpdateStatusDate() {
        return updateStatusDate;
    }

    public void setUpdateStatusDate(Date updateStatusDate) {
        this.updateStatusDate = updateStatusDate;
    }

    public Date getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(Date deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public List<OrderItemEntity> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemEntity> orderItems) {
        this.orderItems = orderItems;
    }

    public Integer getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(Integer totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public BigDecimal getShippingCost() {
        return shippingCost;
    }

    public void setShippingCost(BigDecimal shippingCost) {
        this.shippingCost = shippingCost;
    }

    public BigDecimal getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(BigDecimal discountPrice) {
        this.discountPrice = discountPrice;
    }

    public BigDecimal getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(BigDecimal finalPrice) {
        this.finalPrice = finalPrice;
    }

    public String getAppliedVoucher() {
        return appliedVoucher;
    }

    public void setAppliedVoucher(String appliedVoucher) {
        this.appliedVoucher = appliedVoucher;
    }

    public VoucherEntity getVoucher() {
        return voucher;
    }

    public void setVoucher(VoucherEntity voucher) {
        this.voucher = voucher;
    }

    public RecipientEntity getRecipient() {
        return recipient;
    }

    public void setRecipient(RecipientEntity recipient) {
        this.recipient = recipient;
    }

    public AddressEntity getAddress() {
        return address;
    }

    public void setAddress(AddressEntity address) {
        this.address = address;
    }

    public PaymentEntity getPayment() {
        return payment;
    }

    public void setPayment(PaymentEntity payment) {
        this.payment = payment;
    }
}
