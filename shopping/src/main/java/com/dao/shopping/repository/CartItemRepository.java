package com.dao.shopping.repository;

import com.dao.shopping.entity.CartEntity;
import com.dao.shopping.entity.CartItemEntity;
import com.dao.shopping.entity.ProductVariantEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface CartItemRepository extends JpaRepository<CartItemEntity, Integer> {

    Page<CartItemEntity> findByCart(CartEntity cartEntity, Pageable pageable);

    boolean existsByVariant(ProductVariantEntity variant);

    boolean existsByVariantIn(Set<ProductVariantEntity> variants);

}