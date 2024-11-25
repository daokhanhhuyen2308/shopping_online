package com.dao.shopping.repository;


import com.dao.shopping.entity.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<TokenEntity, Integer> {

    @Query("""
        select t from TokenEntity t inner join t.user u
        where u.id = :userId and (t.isExpired = false or t.isRevoke = false)
        """)
    List<TokenEntity> findAllValidTokensByUserId(@Param("userId") Integer userId);

    Optional<TokenEntity> findByToken(String tokenEntity);

}
