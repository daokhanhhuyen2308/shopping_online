package com.dao.shopping.mapper;

import com.dao.shopping.dto.requests.ProductCreationRequest;
import com.dao.shopping.dto.responses.BrandResponse;
import com.dao.shopping.dto.responses.CategoryResponse;
import com.dao.shopping.dto.responses.ProductResponse;
import com.dao.shopping.entity.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class ProductMapperTest {

    @Test
    void test_toProductDTOResponse() {

        LocalDateTime fixedTime = LocalDateTime.of(2024, 6, 4, 7, 25, 48);

        ColorEntity colorEntity = new ColorEntity();
        colorEntity.setId(1);
        colorEntity.setName("Black");
        SizeEntity sizeEntity = new SizeEntity();
        sizeEntity.setId(1);
        sizeEntity.setName("S");
        Set<BrandEntity> brandEntities = new HashSet<>();
        BrandEntity brandEntity = new BrandEntity();
        brandEntity.setId(1);
        brandEntity.setBrandName("Gucci");
        brandEntities.add(brandEntity);
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setId(1);
        categoryEntity.setName("T-Shirt");

        //given

                ProductEntity productEntity = new ProductEntity();
                productEntity.setId(1);
                productEntity.setName("name");
                productEntity.setCategory(categoryEntity);
                productEntity.setImage("image");
                productEntity.setPrice(BigDecimal.valueOf(10.0));
                productEntity.setDescription("description");
                productEntity.setBrands(brandEntities);
                productEntity.setVariants(new HashSet<>());
                productEntity.setTotalQuantityReceived(1);
                productEntity.setTotalSoldQuantity(0);
                productEntity.setAvailable(true);
                productEntity.setCreatedDate(fixedTime);
                productEntity.setLastModifiedDate(fixedTime);
                productEntity.setCreatedBy("admin");
                productEntity.setLastModifiedBy("admin");
                productEntity.setSoldOut(false);

        BrandResponse brandResponse = new BrandResponse();
        brandResponse.setId(1);
        brandResponse.setName("Gucci");

        Set<BrandResponse> brandResponses = Set.of(brandResponse);

        //when
        ProductResponse expected = ProductResponse.builder().id(1).totalQuantityReceived(10).name("name")
                .price(BigDecimal.valueOf(10.0)).createdDate(fixedTime).lastModifiedDate(fixedTime).image("image")
                .category(CategoryResponse.builder().id(1).name("T-Shirt").build())
                .variants(new HashSet<>())
                .brands(brandResponses).build();


        ProductResponse actual = ProductMapper.toProductDTOResponse(productEntity);

        //then
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void test_toProduct() {

        //given
        ProductCreationRequest request = new ProductCreationRequest();
        request.setBrandIds(Set.of(1));
        request.setCategoryId(1);
        request.setDescription("description");
        request.setName("name");
        request.setPrice(10.0);
        request.setVariantDTOSet(new HashSet<>());
        request.setImage("image");

        //when

        ProductEntity expected = new ProductEntity();
        expected.setDescription("description");
        expected.setImage("image");
        expected.setName("name");
        expected.setPrice(BigDecimal.valueOf(10.0));

        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setId(1);

        BrandEntity brandEntity = new BrandEntity();
        brandEntity.setId(1);

        Set<BrandEntity> brandEntities = new HashSet<>();
        brandEntities.add(brandEntity);

        expected.setCategory(categoryEntity);
        expected.setBrands(brandEntities);

        ProductEntity actual = ProductMapper.toProduct(request);

        //then
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }
}