package com.dao.shopping.repository;

import com.dao.shopping.entity.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface AddressRepository extends JpaRepository<AddressEntity, Integer> {

    @Query("select add from AddressEntity add left join fetch add.recipients r inner join add.user u "+
            "where u.id = :userId order by u.id asc")
    Optional<AddressEntity> findFirstByUserId(@Param("userId") Integer userId);

}
