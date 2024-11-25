package com.dao.shopping.mapper;


import com.dao.shopping.dto.responses.RecipientResponse;
import com.dao.shopping.entity.RecipientEntity;

public class RecipientMapper {

    public static RecipientResponse toRecipientResponse(RecipientEntity recipientEntity){
        RecipientResponse recipientResponse = new RecipientResponse();
        recipientResponse.setId(recipientEntity.getId());
        recipientResponse.setName(recipientEntity.getName());
        recipientResponse.setPhone(recipientEntity.getPhone());
        recipientResponse.getAudit().setCreatedBy(recipientEntity.getCreatedBy());
        recipientResponse.getAudit().setCreatedDate(recipientEntity.getCreatedDate());
        recipientResponse.getAudit().setLastModifiedBy(recipientEntity.getLastModifiedBy());
        recipientResponse.getAudit().setLastModifiedDate(recipientEntity.getLastModifiedDate());
        return recipientResponse;
    }
}
