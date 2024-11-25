package com.dao.shopping.controller;


import com.dao.shopping.dto.responses.ApiResponse;
import com.dao.shopping.dto.responses.ColorResponse;
import com.dao.shopping.entity.ColorEntity;
import com.dao.shopping.repository.ColorRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/color")
public class ColorController {

    private final ColorRepository colorRepository;
    private final ModelMapper modelMapper;

    @GetMapping("/all-colors")
    public ApiResponse<List<ColorResponse>> getAllColors() {

        List<ColorEntity> colorEntities = colorRepository.findAll();

        List<ColorResponse> colorResponses = colorEntities.stream()
                .map((element) -> modelMapper.map(element, ColorResponse.class))
                .toList();

        return ApiResponse.<List<ColorResponse>>builder()
                .message("Successfully retrieved all colors")
                .result(colorResponses)
                .build();
    }

    @GetMapping("/{colorId}")
    public ApiResponse<ColorResponse> getColorById(@PathVariable Integer colorId) {
        return ApiResponse.<ColorResponse>builder()
                .result(modelMapper.map(colorRepository.findById(colorId).orElse(null),ColorResponse.class))
                .build();
    }
}
