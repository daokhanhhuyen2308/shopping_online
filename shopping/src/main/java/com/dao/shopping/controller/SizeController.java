package com.dao.shopping.controller;


import com.dao.shopping.dto.responses.ApiResponse;
import com.dao.shopping.dto.responses.SizeResponse;
import com.dao.shopping.entity.SizeEntity;
import com.dao.shopping.repository.SizeRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/size")
public class SizeController {
    private final SizeRepository sizeRepository;

    private final ModelMapper modelMapper;

    @GetMapping("/all-sizes")
    public ApiResponse<List<SizeResponse>> getAllColors() {

        List<SizeEntity> sizeEntities = sizeRepository.findAll();

        List<SizeResponse> sizeResponses = sizeEntities.stream()
                .map((element) -> modelMapper.map(element, SizeResponse.class))
                .toList();

        return ApiResponse.<List<SizeResponse>>builder()
                .message("Successfully retrieved all colors")
                .result(sizeResponses)
                .build();
    }

    @GetMapping("/{sizeId}")
    public ApiResponse<SizeResponse> getSizeById(@PathVariable int sizeId){
        return ApiResponse.<SizeResponse>builder()
                .message("Successfully retrieved size by id")
                .result(modelMapper.map(sizeRepository.findById(sizeId).orElse(null), SizeResponse.class))
                .build();
    }
}
