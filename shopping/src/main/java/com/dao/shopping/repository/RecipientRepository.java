package com.dao.shopping.repository;

import com.dao.shopping.entity.RecipientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecipientRepository extends JpaRepository<RecipientEntity, Integer> {

    @Query("select r from RecipientEntity r inner join r.user u inner join r.addresses add "+
            "where u.id = :userId and add.id = :addressId")
    Optional<RecipientEntity> findFirstByUserIdAndAddressId(@Param("userId") Integer userId, @Param("addressId") Integer addressId);

}
