package com.dao.shopping.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Set;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "color")
public class ColorEntity extends BaseEntity{
    String name;

    @OneToMany(mappedBy = "color", cascade = CascadeType.ALL)
    Set<ProductVariantEntity> variants;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<ProductVariantEntity> getVariants() {
        return variants;
    }

    public void setVariants(Set<ProductVariantEntity> variants) {
        this.variants = variants;
    }
}
