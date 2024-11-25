package com.dao.shopping.repository.criteria;


import com.dao.shopping.dto.requests.ProductFilterRequest;
import com.dao.shopping.entity.ProductEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class ProductCriteria {
    private final EntityManager em;

    public PageImpl<ProductEntity> searchProduct(ProductFilterRequest filter, Pageable pageable) {

        StringBuilder query = new
        StringBuilder("select distinct p from Product p left join p.brands b left join p.category c inner join p.variants v where 1=1");

        Map<String, Object> param = new HashMap<>();

        if (filter.getName() != null){
            query.append(" and p.name like :name");
            param.put("name", "%" +filter.getName() + "%");
        }

        if (filter.getBrandName() != null){
            query.append(" and b.brandName like :brandName");
            param.put("brandName", "%" +filter.getBrandName().trim() + "%");
        }

        if (filter.getCategory() != null){
            query.append(" and c.name = :category");
            param.put("category", filter.getCategory());
        }

        if (filter.getMinPrice() != null){
            query.append(" and p.price >= :minPrice");
            param.put("minPrice", filter.getMinPrice());
        }

        if (filter.getMaxPrice() != null){
            query.append(" and p.price <= :maxPrice");
            param.put("maxPrice", filter.getMaxPrice());
        }

        if (filter.getColor() != null){
            query.append(" and v.color.name = :color");
            param.put("color", filter.getColor());
        }

        TypedQuery<ProductEntity> typedQuery = em.createQuery(query.toString(), ProductEntity.class);
        Query countQuery = em.createQuery(query.toString().replace("select distinct p", "select count(distinct p.id)"));


        param.forEach((k, v) -> {
            typedQuery.setParameter(k, v);
            countQuery.setParameter(k, v);
        });

        typedQuery.setFirstResult(pageable.getPageNumber());
        typedQuery.setMaxResults(pageable.getPageSize());

        Long countProduct = (Long) countQuery.getSingleResult();
        List<ProductEntity> productEntityList = typedQuery.getResultList();

        return new PageImpl<>(productEntityList, pageable, countProduct);

    }
}
