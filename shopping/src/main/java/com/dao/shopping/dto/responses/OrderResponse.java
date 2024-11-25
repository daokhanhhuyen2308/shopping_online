package com.dao.shopping.dto.responses;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Builder
public class OrderResponse {
    private Integer orderId;
    private List<CartItemResponse> productList;
    private Integer totalQuantity;
    private BigDecimal totalPrice;
    private BigDecimal shippingCost;
    private BigDecimal finalPrice;
    private String orderStatus;
    private String deliveryTime;
    private PaymentResponse payment;
    private boolean paymentSuccess;
    private String paymentAnnounce;
    private AddressResponse deliveryAddress;
    private RecipientResponse recipient;
    private VoucherResponse voucher;
    private LocalDateTime createdDate;
    private String createdBy;
    private LocalDateTime lastModifiedDate;
    private String lastModifiedBy;
    private Boolean deleted;

    public OrderResponse() {
    }

    public OrderResponse(Integer orderId, List<CartItemResponse> productList, Integer totalQuantity,
                         BigDecimal totalPrice, BigDecimal shippingCost, BigDecimal finalPrice, String orderStatus,
                         String deliveryTime, PaymentResponse payment, boolean paymentSuccess, String paymentAnnounce,
                         AddressResponse deliveryAddress, RecipientResponse recipient, VoucherResponse voucher,
                         LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate,
                         String lastModifiedBy, Boolean deleted) {
        this.orderId = orderId;
        this.productList = productList;
        this.totalQuantity = totalQuantity;
        this.totalPrice = totalPrice;
        this.shippingCost = shippingCost;
        this.finalPrice = finalPrice;
        this.orderStatus = orderStatus;
        this.deliveryTime = deliveryTime;
        this.payment = payment;
        this.paymentSuccess = paymentSuccess;
        this.paymentAnnounce = paymentAnnounce;
        this.deliveryAddress = deliveryAddress;
        this.recipient = recipient;
        this.voucher = voucher;
        this.createdDate = createdDate;
        this.createdBy = createdBy;
        this.lastModifiedDate = lastModifiedDate;
        this.lastModifiedBy = lastModifiedBy;
        this.deleted = deleted;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public List<CartItemResponse> getProductList() {
        return productList;
    }

    public void setProductList(List<CartItemResponse> productList) {
        this.productList = productList;
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

    public BigDecimal getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(BigDecimal finalPrice) {
        this.finalPrice = finalPrice;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public PaymentResponse getPayment() {
        return payment;
    }

    public void setPayment(PaymentResponse payment) {
        this.payment = payment;
    }

    public boolean isPaymentSuccess() {
        return paymentSuccess;
    }

    public void setPaymentSuccess(boolean paymentSuccess) {
        this.paymentSuccess = paymentSuccess;
    }

    public String getPaymentAnnounce() {
        return paymentAnnounce;
    }

    public void setPaymentAnnounce(String paymentAnnounce) {
        this.paymentAnnounce = paymentAnnounce;
    }

    public AddressResponse getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(AddressResponse deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public RecipientResponse getRecipient() {
        return recipient;
    }

    public void setRecipient(RecipientResponse recipient) {
        this.recipient = recipient;
    }

    public VoucherResponse getVoucher() {
        return voucher;
    }

    public void setVoucher(VoucherResponse voucher) {
        this.voucher = voucher;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}
