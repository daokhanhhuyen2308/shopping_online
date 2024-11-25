package com.dao.shopping.controller;


import com.dao.shopping.dto.requests.BrandDTORequest;
import com.dao.shopping.dto.responses.ApiResponse;
import com.dao.shopping.dto.responses.BrandResponse;
import com.dao.shopping.service.IBrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/brand")
public class BrandController {

    private final IBrandService iBrandService;

    @PostMapping("/add")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public BrandResponse addNewBrand(@RequestBody BrandDTORequest request){
        return iBrandService.createNewBrand(request);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public BrandResponse updateBrand(@PathVariable int id, @RequestBody BrandDTORequest request){
        return iBrandService.updateBrand(id, request);
    }

    @GetMapping("/all-brands")
    public ApiResponse<List<BrandResponse>> getAllBrands(){

         return ApiResponse.<List<BrandResponse>>builder()
                 .message("OK")
                 .result(iBrandService.getAllBrands())
                 .build();
    }

}
