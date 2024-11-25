package com.dao.shopping.mapper;


import com.dao.shopping.dto.requests.BrandDTORequest;
import com.dao.shopping.dto.responses.BrandResponse;
import com.dao.shopping.entity.BrandEntity;

public class BrandMapper {

    public static BrandResponse toBrandDTOResponse(BrandEntity brandEntity){
        return BrandResponse.builder().id(brandEntity.getId()).name(brandEntity.getBrandName()).build();
    }

    public static BrandEntity toBrandEntity(BrandDTORequest request){
        return BrandEntity.builder().brandName(request.getBrandName()).build();
    }

}
