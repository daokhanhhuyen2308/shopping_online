package com.dao.shopping.repository;

import com.dao.shopping.entity.BrandEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface BrandRepository extends JpaRepository<BrandEntity, Integer> {

    boolean existsByBrandName(String brandName);

    @Query("select count(b) from BrandEntity b inner join b.products p where p.id = :productId")
    int countBrandsByProductId(@Param("productId") int productId);

}
