package com.dao.shopping.dto.responses;


import com.dao.shopping.enums.PaymentType;

import java.math.BigDecimal;


public class PaymentResponse {
    private Integer id;
    private BigDecimal amount;
    private PaymentType paymentType;
    private AuditResponse audit;

    public PaymentResponse() {
    }

    public PaymentResponse(Integer id, BigDecimal amount, PaymentType paymentType) {
        this.id = id;
        this.amount = amount;
        this.paymentType = paymentType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public AuditResponse getAudit() {
        return audit;
    }

    public void setAudit(AuditResponse audit) {
        this.audit = audit;
    }
}
