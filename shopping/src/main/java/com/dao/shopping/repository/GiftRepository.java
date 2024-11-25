package com.dao.shopping.repository;

import com.dao.shopping.entity.GiftEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GiftRepository extends JpaRepository<GiftEntity, Integer> {
}
