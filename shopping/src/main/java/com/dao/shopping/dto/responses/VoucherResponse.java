package com.dao.shopping.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoucherResponse {
    private Integer id;
    private String code;
    private String description;
    private Float discountPercentage;
    private BigDecimal discountAmount;
    private Date startDate;
    private Date endDate;
    private Integer minOrderValue;
    private Boolean freeShipping;
    private GiftDTOInfo giftDTOInfo;
    private AuditResponse audit;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Float getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(Float discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Integer getMinOrderValue() {
        return minOrderValue;
    }

    public void setMinOrderValue(Integer minOrderValue) {
        this.minOrderValue = minOrderValue;
    }

    public Boolean getFreeShipping() {
        return freeShipping;
    }

    public void setFreeShipping(Boolean freeShipping) {
        this.freeShipping = freeShipping;
    }

    public GiftDTOInfo getGiftDTOInfo() {
        return giftDTOInfo;
    }

    public void setGiftDTOInfo(GiftDTOInfo giftDTOInfo) {
        this.giftDTOInfo = giftDTOInfo;
    }

    public AuditResponse getAudit() {
        return audit;
    }

    public void setAudit(AuditResponse audit) {
        this.audit = audit;
    }
}
