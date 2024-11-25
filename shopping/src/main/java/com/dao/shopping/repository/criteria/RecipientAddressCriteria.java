package com.dao.shopping.repository.criteria;


import com.dao.shopping.dto.requests.AddressRequest;
import com.dao.shopping.dto.requests.RecipientRequest;
import com.dao.shopping.entity.AddressEntity;
import com.dao.shopping.entity.RecipientEntity;
import com.dao.shopping.entity.UserEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class RecipientAddressCriteria {

    private final EntityManager em;

    public AddressEntity findExistingAddress(AddressRequest request, UserEntity userEntity){

        StringBuilder query = new StringBuilder("select distinct add from Address add");
        query.append(" where add.account.id = :accountId and 1=1");

        Map<String, Object> params = new HashMap<>();
        params.put("accountId", userEntity.getId());

        if (request.getHouseNumber() != null){
            query.append(" and add.houseNumber = :houseNumber");
            params.put("houseNumber", request.getHouseNumber());
        }

        if (request.getStreet() != null){
            query.append(" and add.street like :street");
            params.put("street", "%" + request.getStreet() + "%");
        }

        if (request.getDistrict() != null){
            query.append(" and add.district like :district");
            params.put("district", "%" + request.getDistrict() + "%");
        }

        if (request.getCity() != null){
            query.append(" and add.city like :city");
            params.put("city", "%" + request.getCity() + "%");
        }

        if (request.getZip() != null){
            query.append(" and add.zip = :zip");
            params.put("zip", request.getZip());
        }

        if (request.getCountry() != null){
            query.append(" and add.country like :country");
            params.put("country", "%" + request.getCountry() + "%");
        }

        TypedQuery<AddressEntity> typedQuery = em.createQuery(query.toString(), AddressEntity.class);

        params.forEach(typedQuery::setParameter);

        List<AddressEntity> addressEntities = typedQuery.getResultList();

        return addressEntities.isEmpty() ? null : addressEntities.getFirst();

    }

    public RecipientEntity findExistingRecipient(RecipientRequest recipient, UserEntity userEntityLoggedIn) {

        StringBuilder query = new StringBuilder("select distinct r from Recipient r where r.account.id = :accountId and 1=1");

        Map<String, Object> params = new HashMap<>();
        params.put("accountId", userEntityLoggedIn.getId());

        if (recipient != null){

            if (recipient.getName() != null){
                query.append(" and r.name like :name");
                params.put("name", "%"+ recipient.getName() +"%");
            }

            if (recipient.getPhone() != null){
                query.append(" and r.phone = :phone");
                params.put("phone", "%" +recipient.getPhone() + "%");
            }
        }

        TypedQuery<RecipientEntity> typedQuery = em.createQuery(query.toString(), RecipientEntity.class);
        params.forEach(typedQuery::setParameter);
        List<RecipientEntity> recipientEntities = typedQuery.getResultList();

        return recipientEntities.isEmpty() ? null : recipientEntities.getFirst();
    }
}
