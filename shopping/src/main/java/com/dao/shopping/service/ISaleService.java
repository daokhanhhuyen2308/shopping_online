package com.dao.shopping.service;


import com.dao.shopping.dto.requests.SaleRequest;
import com.dao.shopping.dto.responses.SaleResponse;

public interface ISaleService {
    SaleResponse createSale(SaleRequest request);

    SaleResponse getProductsBySaleType(String type);
}
