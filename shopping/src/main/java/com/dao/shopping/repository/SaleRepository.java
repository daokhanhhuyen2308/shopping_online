package com.dao.shopping.repository;

import com.dao.shopping.entity.SaleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface SaleRepository extends JpaRepository<SaleEntity, Integer> {

    @Query("select s from SaleEntity s "+
            "where s.type = :type and s.startDate <= :currentDate and s.endDate >= :currentDate and s.isActive = true " +
            "order by s.discountPercent desc")
    Optional<SaleEntity> findSaleProductsByType(@Param("type") String type, @Param("currentDate") LocalDateTime currentDate);
}
