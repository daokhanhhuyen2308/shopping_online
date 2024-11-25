package com.dao.shopping.repository;


import com.dao.shopping.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByEmail(String email);

    @Query("select distinct u from UserEntity u inner join u.roles r where r.name = :name")
    Optional<UserEntity> findUserByRoleId(@Param("name") String name);
}
