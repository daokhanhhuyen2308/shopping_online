package com.dao.shopping.mapper;

import com.dao.shopping.dto.responses.AddressResponse;
import com.dao.shopping.entity.AddressEntity;

public class AddressMapper {

    public static AddressResponse mapAddressToAddressResponse(AddressEntity addressEntity){
        AddressResponse addressResponse = new AddressResponse();
        addressResponse.setId(addressEntity.getId());
        addressResponse.setHouseNumber(addressEntity.getHouseNumber());
        addressResponse.setCity(addressEntity.getCity());
        addressResponse.setCountry(addressEntity.getCountry());
        addressResponse.setStreet(addressEntity.getStreet());
        addressResponse.setDistrict(addressEntity.getDistrict());
        addressResponse.setZip(addressEntity.getZip());
        addressResponse.getAudit().setCreatedBy(addressEntity.getCreatedBy());
        addressResponse.getAudit().setCreatedDate(addressEntity.getCreatedDate());
        addressResponse.getAudit().setLastModifiedBy(addressEntity.getLastModifiedBy());
        addressResponse.getAudit().setLastModifiedDate(addressEntity.getLastModifiedDate());
        return addressResponse;
    }
}
