package com.dao.shopping.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private Integer id;
    private String name;
    private String image;
    private String description;
    private BigDecimal price;
    private String slug;
    private boolean productSale;
    private Float salePercentage;
    private Set<BrandResponse> brands;
    private CategoryResponse category;
    private Set<ProductVariantResponse> variants;
    private Integer totalQuantityReceived;
    private Integer totalSoldQuantity;
    private boolean isSoldOutProduct;
    private LocalDateTime createdDate;
    private String createdBy;
    private LocalDateTime lastModifiedDate;
    private String lastModifiedBy;
    private Boolean deleted;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public boolean isProductSale() {
        return productSale;
    }

    public void setProductSale(boolean productSale) {
        this.productSale = productSale;
    }

    public Float getSalePercentage() {
        return salePercentage;
    }

    public void setSalePercentage(Float salePercentage) {
        this.salePercentage = salePercentage;
    }

    public Set<BrandResponse> getBrands() {
        return brands;
    }

    public void setBrands(Set<BrandResponse> brands) {
        this.brands = brands;
    }

    public CategoryResponse getCategory() {
        return category;
    }

    public void setCategory(CategoryResponse category) {
        this.category = category;
    }

    public Set<ProductVariantResponse> getVariants() {
        return variants;
    }

    public void setVariants(Set<ProductVariantResponse> variants) {
        this.variants = variants;
    }

    public Integer getTotalQuantityReceived() {
        return totalQuantityReceived;
    }

    public void setTotalQuantityReceived(Integer totalQuantityReceived) {
        this.totalQuantityReceived = totalQuantityReceived;
    }

    public Integer getTotalSoldQuantity() {
        return totalSoldQuantity;
    }

    public void setTotalSoldQuantity(Integer totalSoldQuantity) {
        this.totalSoldQuantity = totalSoldQuantity;
    }

    public boolean isSoldOutProduct() {
        return isSoldOutProduct;
    }

    public void setSoldOutProduct(boolean soldOutProduct) {
        isSoldOutProduct = soldOutProduct;
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
