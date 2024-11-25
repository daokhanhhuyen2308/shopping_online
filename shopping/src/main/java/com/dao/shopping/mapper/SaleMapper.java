package com.dao.shopping.mapper;


import com.dao.shopping.dto.requests.SaleRequest;
import com.dao.shopping.dto.responses.SaleResponse;
import com.dao.shopping.entity.SaleEntity;

public class SaleMapper {

    public static SaleResponse mapSaleToSaleResponse(SaleEntity saleEntity){
        SaleResponse saleResponse = new SaleResponse();
        saleResponse.setSaleId(saleEntity.getId());
        saleResponse.setCode(saleEntity.getCode());
        saleResponse.setDescription(saleEntity.getDescription());
        saleResponse.setType(saleEntity.getType());
        saleResponse.setDiscountPercent(saleEntity.getDiscountPercent());
        saleResponse.setDiscountAmount(saleEntity.getDiscountAmount());
        saleResponse.setStartDate(saleEntity.getStartDate());
        saleResponse.setEndDate(saleEntity.getEndDate());
        saleResponse.setProductSales(saleEntity.getProducts().stream()
                .map(ProductMapper::toProductDTOResponse)
                .toList());
        saleResponse.setActive(saleEntity.isActive());
        return saleResponse;
    }

    public static SaleEntity mapSaleRequestToSale(SaleRequest saleRequest){
        SaleEntity saleEntity = new SaleEntity();
        saleEntity.setCode(saleRequest.getCode());
        saleEntity.setDescription(saleRequest.getDescription());
        saleEntity.setType(saleRequest.getType());
        saleEntity.setDiscountPercent(saleRequest.getDiscountPercent());
        saleEntity.setDiscountAmount(saleRequest.getDiscountAmount());
        saleEntity.setStartDate(saleRequest.getStartDate());
        saleEntity.setEndDate(saleRequest.getEndDate());
        return saleEntity;
    }
}
