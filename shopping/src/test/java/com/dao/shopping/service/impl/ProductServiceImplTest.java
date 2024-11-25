package com.dao.shopping.service.impl;

import com.dao.shopping.dto.requests.ProductCreationRequest;
import com.dao.shopping.dto.requests.ProductVariantRequest;
import com.dao.shopping.dto.responses.*;
import com.dao.shopping.entity.*;
import com.dao.shopping.exception.CustomExceptionHandler;
import com.dao.shopping.mapper.BrandMapper;
import com.dao.shopping.repository.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @InjectMocks
    private ProductServiceImpl productService;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private ColorRepository colorRepository;
    @Mock
    private SizeRepository sizeRepository;
    @Mock
    private BrandRepository brandRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private SaleRepository saleRepository;

    ColorEntity colorEntity;
    SizeEntity sizeEntity;
    BrandEntity brandEntity;
    CategoryEntity categoryEntity;
    LocalDateTime fixedTime;

    @BeforeEach
    void setUp(){
        colorEntity = new ColorEntity();
        colorEntity.setId(1);
        colorEntity.setName("Black");

        sizeEntity = new SizeEntity();
        sizeEntity.setId(1);
        sizeEntity.setName("S");

        brandEntity = new BrandEntity();
        brandEntity.setId(1);
        brandEntity.setBrandName("Gucci");

        categoryEntity = new CategoryEntity();
        categoryEntity.setId(1);
        categoryEntity.setName("T-Shirt");

        fixedTime = LocalDateTime.of(2024, 6, 4, 7, 25, 48);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void test_CreateProduct_Success() {

        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);

        //bad test case
        Set<BrandEntity> brandEntities = new HashSet<>();
        brandEntities.add(brandEntity);

        //given
        Set<ProductVariantRequest> productVariantRequestSet = Set.of(
                ProductVariantRequest.builder().sizeId(1).colorId(1).quantity(1).build()
                );

        Set<Integer> brandIds = new HashSet<>();
        brandIds.add(1);

        ProductCreationRequest request = ProductCreationRequest.builder()
                .name("name").categoryId(1).brandIds(brandIds)
                .image("image").price(10.0)
                .description("description")
                .variantDTOSet(productVariantRequestSet).build();

        ProductVariantEntity productVariantEntity = new ProductVariantEntity();
        productVariantEntity.setId(1);
        productVariantEntity.setColor(colorEntity);
        productVariantEntity.setSize(sizeEntity);
        productVariantEntity.setSoldOut(false);
        productVariantEntity.setAvailable(true);

        Set<ProductVariantEntity> variants = Set.of(productVariantEntity);

        ProductEntity productEntity = new ProductEntity();
        productEntity.setId(1);
        productEntity.setName("name");
        productEntity.setBrands(brandEntities);
        productEntity.setCategory(categoryEntity);
        productEntity.setImage("image");
        productEntity.setPrice(BigDecimal.valueOf(10.0));
        productEntity.setDescription("description");
        productEntity.setVariants(variants);
        productEntity.setCreatedDate(fixedTime);
        productEntity.setLastModifiedDate(fixedTime);
        productEntity.setTotalQuantityReceived(1);
        productEntity.setTotalSoldQuantity(0);
        productEntity.setSoldOut(false);

        BrandEntity brandEntityExpected = new BrandEntity();
        brandEntityExpected.setId(1);
        brandEntityExpected.setBrandName("Gucci");
        brandEntityExpected.getProducts().add(productEntity);

        SaleEntity saleEntityExpected = new SaleEntity();
        saleEntityExpected.setId(1);
        saleEntityExpected.setActive(true);
        saleEntityExpected.setCode("SALE");

        //when
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("admin");
        SecurityContextHolder.setContext(securityContext);
        when(colorRepository.findById(anyInt())).thenReturn(Optional.of(colorEntity));
        when(sizeRepository.findById(anyInt())).thenReturn(Optional.of(sizeEntity));
        when(categoryRepository.findById(request.getCategoryId())).thenReturn(Optional.of(categoryEntity));
        when(brandRepository.findById(anyInt())).thenReturn(Optional.of(brandEntityExpected));
        when(saleRepository.findById(anyInt())).thenReturn(Optional.of(saleEntityExpected));

        ProductResponse actual = productService.createProduct(request);

        SizeResponse sizeResponse = SizeResponse.builder().id(1).name("S").build();
        ColorResponse colorResponse = ColorResponse.builder().id(1).name("Black").build();

        Set<ProductVariantResponse> variantNameDTOSet = Set.of(
                ProductVariantResponse.builder().variantId(1).size(sizeResponse).color(colorResponse).quantity(1).build()
        );

        Set<BrandResponse> brandResponses = brandEntities.stream().map(BrandMapper::toBrandDTOResponse).collect(Collectors.toSet());
        CategoryResponse categoryResponse = CategoryResponse.builder().id(1).name("T-Shirt").build();

        ProductResponse expected = ProductResponse.builder()
                .id(1)
                .name("name").image("image")
                .description("description")
                .price(BigDecimal.valueOf(10.0)).brands(brandResponses).category(categoryResponse)
                .totalSoldQuantity(0)
                .variants(variantNameDTOSet).totalQuantityReceived(1).build();

        //then-verify
        assertThat(actual.getId()).isEqualTo(expected.getId());


    }

    @Test
    void test_CreateProduct_Failure_NullProductName(){

        ProductCreationRequest add = ProductCreationRequest.builder().name(null).image("image").price(10.0).description("description")
                .brandIds(new HashSet<>()).categoryId(1).variantDTOSet(new HashSet<>()).build();

        CustomExceptionHandler ex =  assertThrows(CustomExceptionHandler.class, () -> productService.createProduct(add));

        assertEquals("Product name is not null", ex.getError().getMessage());
    }

    @Test
    void test_CreateProduct_Failure_NullProductImage(){

        ProductCreationRequest add = ProductCreationRequest.builder().name("name").image(null).price(10.0).description("description")
                .brandIds(new HashSet<>()).categoryId(1).variantDTOSet(new HashSet<>()).build();

        CustomExceptionHandler ex =  assertThrows(CustomExceptionHandler.class, () ->
            productService.createProduct(add));

        Assertions.assertEquals("Product image is not null", ex.getError().getMessage());

    }

    @Test
    void test_CreateProduct_Failure_NullCategory(){

        Set<ProductVariantRequest> productVariantRequestSet = Set.of(ProductVariantRequest.builder()
                .sizeId(1).colorId(1).quantity(1).build(),
                ProductVariantRequest.builder().sizeId(2).colorId(2).quantity(2).build());

        ProductCreationRequest add = ProductCreationRequest.builder().name("name").image("image").price(10.0).description("description")
                .brandIds(new HashSet<>()).categoryId(null).variantDTOSet(productVariantRequestSet).build();

        CustomExceptionHandler ex =  assertThrows(CustomExceptionHandler.class, () ->
            productService.createProduct(add)
        );

        Assertions.assertEquals("Category is not null", ex.getError().getMessage());

    }

    @Test
    void test_CreateProduct_Failure_NullBrand(){
        Set<ProductVariantRequest> productVariantRequestSet = Set.of(ProductVariantRequest.builder()
                        .sizeId(1).colorId(1).quantity(1).build(),
                ProductVariantRequest.builder().sizeId(2).colorId(2).quantity(2).build());

        ProductCreationRequest request = ProductCreationRequest.builder().name("name").image("image").price(10.0)
                .brandIds(null)
                .description("description")
                .categoryId(1).variantDTOSet(productVariantRequestSet).build();

        CustomExceptionHandler ex =  assertThrows(CustomExceptionHandler.class, () ->
            productService.createProduct(request));

        Assertions.assertEquals("BrandIds is not null", ex.getError().getMessage());
    }

    @Test
    void test_CreateProduct_Failure_NullDescription(){

        ProductCreationRequest request = ProductCreationRequest.builder().name("name").image("image").price(10.0).description(null)
                .brandIds(new HashSet<>()).categoryId(1).variantDTOSet(new HashSet<>()).build();

        CustomExceptionHandler ex =  assertThrows(CustomExceptionHandler.class, () ->
            productService.createProduct(request)
        );

        Assertions.assertEquals("Description is not null", ex.getError().getMessage());
    }

    @Test
    void test_CreateProduct_Failure_NullPrice(){

        ProductCreationRequest add = ProductCreationRequest.builder().name("name").image("image").price(null).description("description")
                .brandIds(new HashSet<>()).categoryId(1).variantDTOSet(new HashSet<>()).build();

        CustomExceptionHandler ex =  assertThrows(CustomExceptionHandler.class, () ->
            productService.createProduct(add)
        );

        Assertions.assertEquals("Product price is not null", ex.getError().getMessage());
    }

    @Test
    void test_CreateProduct_Failure_NullProductVariant(){

    }

    @Test
    void getAllProduct() {
    }
}