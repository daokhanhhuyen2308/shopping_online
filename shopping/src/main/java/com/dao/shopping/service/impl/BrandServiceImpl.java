package com.dao.shopping.service.impl;


import com.dao.shopping.dto.requests.BrandDTORequest;
import com.dao.shopping.dto.responses.BrandResponse;
import com.dao.shopping.entity.BrandEntity;
import com.dao.shopping.exception.CustomExceptionHandler;
import com.dao.shopping.mapper.BrandMapper;
import com.dao.shopping.repository.BrandRepository;
import com.dao.shopping.service.IBrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements IBrandService {

    private final BrandRepository brandRepository;

    @Override
    public BrandResponse createNewBrand(BrandDTORequest request) {

        String brandName = request.getBrandName();

        if (brandName == null){
            throw CustomExceptionHandler.badRequestException("Brand name is required");
        }

        boolean exists = brandRepository.existsByBrandName(brandName);

        if (exists){
            throw CustomExceptionHandler.badRequestException("Brand name already exists: " + brandName);
        }

        BrandEntity brandEntity = BrandMapper.toBrandEntity(request);

        brandEntity = brandRepository.save(brandEntity);

        return BrandMapper.toBrandDTOResponse(brandEntity);
    }

    @Override
    public BrandResponse updateBrand(Integer id, BrandDTORequest request) {

        BrandEntity brandEntity = brandRepository.findById(id)
                .orElseThrow(() -> CustomExceptionHandler.notFoundException("Brand not found with id: " + id));

        String newBrandName = request.getBrandName();

        if (brandRepository.existsByBrandName(newBrandName)) {
            throw CustomExceptionHandler.badRequestException("Brand name '" + newBrandName + "' already exists.");
        }

        brandEntity.setBrandName(request.getBrandName());

        BrandEntity updatedBrandEntity = brandRepository.save(brandEntity);

        return BrandMapper.toBrandDTOResponse(updatedBrandEntity);
    }

    @Override
    public List<BrandResponse> getAllBrands() {

        List<BrandEntity> brandEntityEntities = brandRepository.findAll();

        return brandEntityEntities.stream().map(BrandMapper::toBrandDTOResponse).toList();
    }
}
