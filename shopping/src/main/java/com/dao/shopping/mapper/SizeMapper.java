package com.dao.shopping.mapper;


import com.dao.shopping.dto.responses.SizeResponse;
import com.dao.shopping.entity.SizeEntity;

public class SizeMapper {
    public static SizeResponse toSizeResponse(SizeEntity sizeEntity) {
        return SizeResponse.builder().id(sizeEntity.getId())
                .name(sizeEntity.getName())
                .build();
    }
}
