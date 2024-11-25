package com.dao.shopping.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.Set;

@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "product")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductEntity extends BaseEntity{
    String name;
    String image;
    String description;
    BigDecimal price;
    BigDecimal discountedPrice;

    @ManyToOne
    @JoinColumn(name = "category_id")
    CategoryEntity category;

    @ManyToMany(mappedBy = "products", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    Set<BrandEntity> brands;

    Integer totalQuantityReceived;

    Integer totalSoldQuantity;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<ProductVariantEntity> variants;

    @Column(name = "is_sold_out", nullable = false)
    boolean isSoldOut;

    @Column(name = "is_available", nullable = false)
    boolean isAvailable = true;

    String slug;

    @ManyToOne
    @JoinColumn(name = "sale_id", referencedColumnName = "id")
    SaleEntity sale;

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

    public BigDecimal getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(BigDecimal discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

    public CategoryEntity getCategory() {
        return category;
    }

    public void setCategory(CategoryEntity category) {
        this.category = category;
    }

    public Set<BrandEntity> getBrands() {
        return brands;
    }

    public void setBrands(Set<BrandEntity> brands) {
        this.brands = brands;
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

    public Set<ProductVariantEntity> getVariants() {
        return variants;
    }

    public void setVariants(Set<ProductVariantEntity> variants) {
        this.variants = variants;
    }

    public boolean isSoldOut() {
        return isSoldOut;
    }

    public void setSoldOut(boolean soldOut) {
        isSoldOut = soldOut;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public SaleEntity getSale() {
        return sale;
    }

    public void setSale(SaleEntity sale) {
        this.sale = sale;
    }
}
