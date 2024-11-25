package com.dao.shopping.repository;

import com.dao.shopping.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Integer> {

    @Query("select p from ProductEntity p where p.totalSoldQuantity >= 100")
    Page<ProductEntity> findBestSellingProducts(Pageable pageable);

    @Query("select p from ProductEntity p where p.isSoldOut = true and p.totalQuantityReceived > p.totalSoldQuantity")
    Page<ProductEntity> findSoldOutProducts(Pageable pageable);

    Optional<ProductEntity> findBySlug(String slug);

}
