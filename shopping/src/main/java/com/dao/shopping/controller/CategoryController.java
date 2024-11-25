package com.dao.shopping.controller;

import com.dao.shopping.dto.responses.ApiResponse;
import com.dao.shopping.dto.responses.CategoryResponse;
import com.dao.shopping.entity.CategoryEntity;
import com.dao.shopping.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/category")
public class CategoryController {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @GetMapping("/all-categories")
    public ApiResponse<List<CategoryResponse>> getAllCategories() {
        List<CategoryEntity> categories = categoryRepository.findAll();

        List<CategoryResponse> categoryResponses = categories.stream()
                .map((element) -> modelMapper.map(element, CategoryResponse.class))
                .toList();

        return ApiResponse.<List<CategoryResponse>>builder()
                .message("OK")
                .result(categoryResponses)
                .build();
    }
}
