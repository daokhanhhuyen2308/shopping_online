package com.dao.shopping.dto.responses;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Builder
public class SaleResponse {
    private Integer saleId;
    private String code;
    private String description;
    private String type;
    private Float discountPercent;
    private BigDecimal discountAmount;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private boolean isActive;
    private List<ProductResponse> productSales;

    public SaleResponse() {
    }

    public SaleResponse(Integer saleId, String code, String description, String type, Float discountPercent,
                        BigDecimal discountAmount, LocalDateTime startDate, LocalDateTime endDate,
                        boolean isActive, List<ProductResponse> productSales) {
        this.saleId = saleId;
        this.code = code;
        this.description = description;
        this.type = type;
        this.discountPercent = discountPercent;
        this.discountAmount = discountAmount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isActive = isActive;
        this.productSales = productSales;
    }

    public Integer getSaleId() {
        return saleId;
    }

    public void setSaleId(Integer saleId) {
        this.saleId = saleId;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Float getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(Float discountPercent) {
        this.discountPercent = discountPercent;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public List<ProductResponse> getProductSales() {
        return productSales;
    }

    public void setProductSales(List<ProductResponse> productSales) {
        this.productSales = productSales;
    }
}
