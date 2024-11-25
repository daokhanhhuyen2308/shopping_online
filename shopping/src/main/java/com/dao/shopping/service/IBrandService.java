package com.dao.shopping.service;

import com.dao.shopping.dto.requests.BrandDTORequest;
import com.dao.shopping.dto.responses.BrandResponse;

import java.util.List;

public interface IBrandService {
    BrandResponse createNewBrand(BrandDTORequest request);

    BrandResponse updateBrand(Integer id, BrandDTORequest request);

    List<BrandResponse> getAllBrands();
}
