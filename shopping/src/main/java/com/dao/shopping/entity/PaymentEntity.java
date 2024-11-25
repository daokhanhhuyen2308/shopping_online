package com.dao.shopping.entity;

import com.dao.shopping.enums.PaymentType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "payment")
public class PaymentEntity extends BaseEntity{

    BigDecimal amount;

    @Enumerated(EnumType.STRING)
    PaymentType paymentType;

    @Column(name = "payment_status")
    boolean paymentStatus = false;

    @OneToMany(mappedBy = "payment")
    List<OrderEntity> orders;

    @ManyToOne
    @JoinColumn(name = "recipient_id")
    RecipientEntity payee;

    @ManyToOne
    @JoinColumn(name = "user_id")
    UserEntity payer;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public boolean isPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(boolean paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public List<OrderEntity> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderEntity> orders) {
        this.orders = orders;
    }

    public RecipientEntity getPayee() {
        return payee;
    }

    public void setPayee(RecipientEntity payee) {
        this.payee = payee;
    }

    public UserEntity getPayer() {
        return payer;
    }

    public void setPayer(UserEntity payer) {
        this.payer = payer;
    }
}

