package com.dao.shopping.service;


import com.dao.shopping.dto.requests.ProductCreationRequest;
import com.dao.shopping.dto.requests.ProductFilterRequest;
import com.dao.shopping.dto.requests.ProductUpdateRequest;
import com.dao.shopping.dto.responses.ProductResponse;
import com.dao.shopping.dto.responses.ProductVariantResponse;
import com.dao.shopping.dto.responses.ResponsePage;

public interface IProductService {

    ProductResponse createProduct(ProductCreationRequest add);

    ResponsePage<ProductResponse> getAllProduct(ProductFilterRequest filter);

    ResponsePage<ProductResponse> getBestSelling(Integer page, Integer size);

    ResponsePage<ProductResponse> getSoldOut(Integer page, Integer size);

    ProductResponse updateProductById(Integer id, ProductUpdateRequest request);

    void deleteProductById(Integer productId);

    void deleteOrDisableProductVariantById(Integer variantId);

    ProductResponse getProductById(Integer productId);

    ProductVariantResponse getProductVariantById(Integer variantId);

    ProductResponse getProductBySlug(String slug);

}
