package com.dao.shopping.mapper;


import com.dao.shopping.dto.responses.ColorResponse;
import com.dao.shopping.entity.ColorEntity;

public class ColorMapper {
    public static ColorResponse toColorResponse(ColorEntity colorEntity) {
        return ColorResponse.builder()
                .id(colorEntity.getId())
                .name(colorEntity.getName())
                .build();
    }
}
