package com.dao.shopping.service.impl;

import com.dao.shopping.dto.requests.SaleRequest;
import com.dao.shopping.dto.responses.SaleResponse;
import com.dao.shopping.entity.SaleEntity;
import com.dao.shopping.exception.CustomExceptionHandler;
import com.dao.shopping.mapper.SaleMapper;
import com.dao.shopping.repository.ProductRepository;
import com.dao.shopping.repository.SaleRepository;
import com.dao.shopping.service.ISaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class SaleServiceImpl implements ISaleService {

    private final SaleRepository saleRepository;
    private final ProductRepository productRepository;

    @Override
    public SaleResponse createSale(SaleRequest saleRequest){
        if (saleRequest.getCode() == null){
            throw CustomExceptionHandler.badRequestException("Sale code is required");
        }

        SaleEntity saleEntity = SaleMapper.mapSaleRequestToSale(saleRequest);
        saleEntity = saleRepository.save(saleEntity);
        return SaleMapper.mapSaleToSaleResponse(saleEntity);

    }

    @Override
    public SaleResponse getProductsBySaleType(String type) {
        Optional<SaleEntity> sale = saleRepository.findSaleProductsByType(type, LocalDateTime.now());
        return sale.map(SaleMapper::mapSaleToSaleResponse)
                .orElseThrow(() -> CustomExceptionHandler.notFoundException("No sale found for type: " + type));
    }



}
