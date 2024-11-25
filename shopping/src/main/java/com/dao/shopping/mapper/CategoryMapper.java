package com.dao.shopping.mapper;


import com.dao.shopping.dto.responses.CategoryResponse;
import com.dao.shopping.entity.CategoryEntity;

public class CategoryMapper {
    public static CategoryResponse toCategoryResponse(CategoryEntity categoryEntity) {
        return CategoryResponse.builder().id(categoryEntity.getId())
                .name(categoryEntity.getName())
                .build();
    }
}
