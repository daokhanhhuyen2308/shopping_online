package com.dao.shopping.service.impl;

import com.dao.shopping.dto.requests.*;
import com.dao.shopping.dto.responses.ProductResponse;
import com.dao.shopping.dto.responses.ProductVariantResponse;
import com.dao.shopping.dto.responses.ResponsePage;
import com.dao.shopping.entity.*;
import com.dao.shopping.exception.CustomExceptionHandler;
import com.dao.shopping.mapper.*;
import com.dao.shopping.repository.*;
import com.dao.shopping.repository.criteria.ProductCriteria;
import com.dao.shopping.service.IProductService;
import com.dao.shopping.utils.SlugUtils;
import io.micrometer.common.util.StringUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProductServiceImpl implements IProductService {

    private final ProductRepository productRepository;
    private final ColorRepository colorRepository;
    private final SizeRepository sizeRepository;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;
    private final ProductCriteria productCriteria;
    private final ProductVariantRepository variantRepository;
    private final CartItemRepository cartItemRepository;
    private final SaleRepository saleRepository;

    @Override
    public ProductResponse createProduct(ProductCreationRequest request) {

        String username = getUsernameFromSecurityContext();

        System.out.println("username: " +username);

        if (StringUtils.isEmpty(request.getName())){
            throw CustomExceptionHandler.badRequestException("Product name is not null");
        }

        if (StringUtils.isEmpty(request.getImage())){
            throw CustomExceptionHandler.badRequestException("Product image is not null");
        }

        if (request.getCategoryId() == null){
            throw CustomExceptionHandler.badRequestException("Category is not null");
        }

        if (StringUtils.isEmpty(request.getDescription())){
            throw CustomExceptionHandler.badRequestException("Description is not null");
        }

        if (request.getPrice() == null){
            throw CustomExceptionHandler.badRequestException("Product price is not null");
        }

        if (request.getPrice() <= 0) {
            throw CustomExceptionHandler.badRequestException("Product price must be greater than 0");
        }

        ProductEntity productEntity = ProductMapper.toProduct(request);
        productEntity.setCreatedDate(LocalDateTime.now());
        productEntity.setCreatedBy(username);
        productEntity.setLastModifiedDate(LocalDateTime.now());
        productEntity.setSoldOut(false);

        Integer totalQuantityReceived = 0;

        //save vao product
        Set<ProductVariantEntity> variants = new HashSet<>();

        if (request.getVariantDTOSet() != null){
            for (ProductVariantRequest variantDTO : request.getVariantDTOSet()){

                totalQuantityReceived += variantDTO.getQuantity();

                productEntity.setTotalQuantityReceived(totalQuantityReceived);

                ProductVariantEntity productVariantEntity = new ProductVariantEntity();

                if (variantDTO.getSizeId() == null){
                    throw CustomExceptionHandler.badRequestException("Size id is not null");
                }

                if (variantDTO.getColorId() == null){
                    throw CustomExceptionHandler.badRequestException("Color id is not null");
                }

                if (variantDTO.getQuantity() == null){
                    throw CustomExceptionHandler.badRequestException("Variant quantity must not be null");
                }

                if (variantDTO.getQuantity() <= 0) {
                    throw CustomExceptionHandler.badRequestException("Variant quantity must be greater than 0");
                }

                ColorEntity optionalColorEntity = colorRepository.findById(variantDTO.getColorId())
                        .orElseThrow(() -> CustomExceptionHandler.notFoundException("Color id's not found"));

                SizeEntity optionalSizeEntity = sizeRepository.findById(variantDTO.getSizeId())
                        .orElseThrow(() -> CustomExceptionHandler.notFoundException("Size id's not found"));

                int variantQuantity = variantDTO.getQuantity();

                productVariantEntity.setProduct(productEntity);
                productVariantEntity.setSize(optionalSizeEntity);
                productVariantEntity.setColor(optionalColorEntity);
                productVariantEntity.setQuantity(variantQuantity);
                productVariantEntity.setSoldQuantity(0);
                productVariantEntity.setSoldOut(false);

                variants.add(productVariantEntity);
                productEntity.setVariants(variants);
            }
        }

        else throw CustomExceptionHandler.badRequestException("Product categoryId is not null");

        if (request.getCategoryId() == null){
            throw CustomExceptionHandler.badRequestException("Category is not null");
        }

        CategoryEntity categoryEntity = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> CustomExceptionHandler.notFoundException("Category's id not found"));
        productEntity.setCategory(categoryEntity);

        Set<Integer> brandIds = request.getBrandIds();

        Set<BrandEntity> brandEntities = new HashSet<>();

        if (brandIds != null){
            for (Integer brandId : brandIds){
                BrandEntity brandEntity = brandRepository.findById(brandId)
                        .orElseThrow(() -> CustomExceptionHandler.notFoundException("Brand's id not found"));

                brandEntities.add(brandEntity);
            }
        }
        else throw CustomExceptionHandler.badRequestException("BrandIds is not null");

        productEntity.setBrands(brandEntities);
        productEntity.setAvailable(true);

        for (BrandEntity brandEntity : brandEntities) {
            brandEntity.getProducts().add(productEntity);
        }

        SaleEntity saleEntity = saleRepository.findById(request.getSaleId())
                .orElseThrow(null);

        float discountPercentage = saleEntity.getDiscountPercent() != null ? saleEntity.getDiscountPercent() : 0.0f;
        productEntity.setDiscountedPrice(productEntity.getPrice().subtract(productEntity.getPrice()
                .multiply(BigDecimal.valueOf(discountPercentage)).divideToIntegralValue(BigDecimal.valueOf(100))));

        productEntity.setSale(saleEntity);

        productEntity.setSlug(SlugUtils.generateSlug(request.getName()));
        productRepository.save(productEntity);

        ProductResponse response = ProductMapper.toProductDTOResponse(productEntity);

        response.setBrands(productEntity.getBrands().stream().map(BrandMapper::toBrandDTOResponse).collect(Collectors.toSet()));
        response.setCategory(CategoryMapper.toCategoryResponse(categoryEntity));

        return response;
    }

    @Override
    public ProductResponse getProductBySlug(String slug) {
        ProductEntity productEntity = productRepository.findBySlug(slug)
                .orElseThrow(() -> CustomExceptionHandler.notFoundException("Product not found with slug" + slug));

        return ProductMapper.toProductDTOResponse(productEntity);
    }

    @Override
    public ResponsePage<ProductResponse> getAllProduct(ProductFilterRequest filter) {

        Pageable pageable = PageRequest.of(filter.getPage(), filter.getSize());

        Page<ProductEntity> productEntities = productCriteria.searchProduct(filter, pageable);

        Page<ProductResponse> productDTOResponsePage = productEntities.map(ProductMapper::toProductDTOResponse);

        List<ProductResponse> list = productDTOResponsePage.getContent();

        ResponsePage<ProductResponse> responsePage = new ResponsePage<>();
        responsePage.setPageSize(pageable.getPageSize());
        responsePage.setPageNumber(pageable.getPageNumber());
        responsePage.setTotalPages(productDTOResponsePage.getTotalPages());
        responsePage.setTotalElements(productDTOResponsePage.getTotalElements());
        responsePage.setContent(list);

        return responsePage;
    }

    @Override
    public ResponsePage<ProductResponse> getSoldOut(Integer page, Integer size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<ProductEntity> productPage = productRepository.findSoldOutProducts(pageable);

        if (productPage.getContent().isEmpty()){
            log.info("There are no products sold out yet");
        }

        return convertProductPageToProductResponsePage(productPage);
    }

    @Override
    public ResponsePage<ProductResponse> getBestSelling(Integer page, Integer size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<ProductEntity> results = productRepository.findBestSellingProducts(pageable);

        if (results.isEmpty()){
            log.info("There are no best selling products yet");
        }

        return convertProductPageToProductResponsePage(results);
    }

    @Override
    public synchronized ProductResponse updateProductById(Integer id, ProductUpdateRequest request) {

        String username = getUsernameFromSecurityContext();

        ProductEntity existingProductEntity = productRepository.findById(id)
                .orElseThrow(() -> CustomExceptionHandler.notFoundException("Product's id is not found"));

        //just update product
        String description = request.getDescription();
        String name = request.getName();
        String image = request.getImage();
        Double price = request.getPrice();

        if (name != null){
            existingProductEntity.setName(name);
        }

        else throw CustomExceptionHandler.badRequestException("Product's name is required");

        if (description != null){
            existingProductEntity.setDescription(description);
        }

        else throw CustomExceptionHandler.badRequestException("Description is required");

        if (image != null){
            existingProductEntity.setImage(image);
        }

        else throw CustomExceptionHandler.badRequestException("Product's image is required");

        if (price != null){
            existingProductEntity.setPrice(BigDecimal.valueOf(price));
        }

        else throw CustomExceptionHandler.badRequestException("Product's price is required");

        if (request.getBrandIds() != null){

            Set<Integer> brandIs = request.getBrandIds();

            for (Integer brandId : brandIs){

                BrandEntity brandEntity = brandRepository.findById(brandId)
                        .orElseThrow(() -> CustomExceptionHandler.notFoundException("Brand's id not found" + brandId));

                existingProductEntity.getBrands().add(brandEntity);
            }
        }

        else throw CustomExceptionHandler.badRequestException("BrandIds are required");

        if (request.getCategoryId() != null){

            CategoryEntity categoryEntity = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> CustomExceptionHandler.notFoundException("Category's id not found" + request.getCategoryId()));

            existingProductEntity.setCategory(categoryEntity);
        }

        else throw CustomExceptionHandler.badRequestException("Category's id of product is required");

        //get old total Quantity of the existing Product before update
        int oldTotalQuantity = existingProductEntity.getTotalQuantityReceived();

        //just update variant
        if (request.getVariant() != null){

            //update variant's information based on variant's id
            ProductVariantUpdate variantUpdate = request.getVariant();
            int variantQuantityUpdate = variantUpdate.getQuantity();

                //check if product variant does not exist -> I will create that variant
                if (variantUpdate.getVariantId() == null){

                    ProductVariantEntity productVariantEntity = new ProductVariantEntity();
                    productVariantEntity.setProduct(existingProductEntity);
                    productVariantEntity.setSize(sizeRepository.findById(variantUpdate.getSizeId())
                            .orElseThrow(() -> CustomExceptionHandler.notFoundException("Size's id not found")));

                    productVariantEntity.setColor(colorRepository.findById(variantUpdate.getColorId())
                            .orElseThrow(() -> CustomExceptionHandler.notFoundException("Color's id not found")));

                    productVariantEntity.setQuantity(variantQuantityUpdate);
                    productVariantEntity.setSoldOut(false);
                    productVariantEntity.setSoldQuantity(0);

                    oldTotalQuantity += variantQuantityUpdate;
                    existingProductEntity.setTotalQuantityReceived(oldTotalQuantity);

                    existingProductEntity.getVariants().add(productVariantEntity);
                    variantRepository.save(productVariantEntity);
                }

                else {
                    //check if product variant existed
                    ProductVariantEntity existingVariant = existingProductEntity.getVariants()
                            .stream()
                            .filter(productVariant -> Objects.equals(variantUpdate.getVariantId(), productVariant.getId()))
                            .findFirst()
                            .orElse(null);

                    if (existingVariant != null) {

                        if (variantUpdate.getSizeId() != null) {
                            SizeEntity sizeEntity = sizeRepository.findById(variantUpdate.getSizeId())
                                    .orElseThrow(() -> CustomExceptionHandler.notFoundException("Size's id not found"));

                            existingVariant.setSize(sizeEntity);
                        }

                        else throw CustomExceptionHandler.badRequestException("Size's id is required");

                        if (variantUpdate.getColorId() != null) {
                            ColorEntity colorEntity = colorRepository.findById(variantUpdate.getColorId())
                                    .orElseThrow(() -> CustomExceptionHandler.notFoundException("Color's id not found"));

                            existingVariant.setColor(colorEntity);
                        }

                        else throw CustomExceptionHandler.badRequestException("Color's id is required");

                        if (variantUpdate.getQuantity() == null) {
                            throw CustomExceptionHandler.badRequestException("Quantity's product variant is required");
                        }

                        else if (variantUpdate.getQuantity() < 0){
                            throw CustomExceptionHandler.badRequestException("Quantity's product variant is not less than 0");
                        }

                        else{

                            if (variantQuantityUpdate == 0) {
                                //error about set old total quantity
                                oldTotalQuantity -= variantUpdate.getQuantity();
                                existingVariant.setSoldOut(true);
                                existingVariant.setQuantity(0);
                                existingProductEntity.setTotalQuantityReceived(oldTotalQuantity);

                                if (cartItemRepository.existsByVariant(existingVariant)){
                                    log.info("This product out of stock");
                                    existingVariant.setAvailable(false);
                                    variantRepository.save(existingVariant);
                                }
                            }

                            else if (variantQuantityUpdate > oldTotalQuantity) {
                                oldTotalQuantity += variantQuantityUpdate - oldTotalQuantity;
                                existingProductEntity.setTotalQuantityReceived(oldTotalQuantity);
                                existingVariant.setSoldOut(false);
                            }

                            else if (variantQuantityUpdate < oldTotalQuantity) {
                                int quantityUpdate = oldTotalQuantity - variantQuantityUpdate;
                                oldTotalQuantity -= quantityUpdate;

                            }

                            existingVariant.setQuantity(variantQuantityUpdate);
                            existingProductEntity.setTotalQuantityReceived(oldTotalQuantity);
                        }
                    }

                    else throw CustomExceptionHandler.notFoundException("Product variant not found with id: " + variantUpdate.getVariantId());
                }

            }

        existingProductEntity.setLastModifiedDate(LocalDateTime.now());
        existingProductEntity.setLastModifiedBy(username);
        existingProductEntity = productRepository.save(existingProductEntity);

        return ProductMapper.toProductDTOResponse(existingProductEntity);
    }

    @Override
    public void deleteProductById(Integer productId) {

        ProductEntity productEntity = productRepository.findById(productId)
                .orElseThrow(() -> CustomExceptionHandler.notFoundException("Product's id not found"));

        Set<ProductVariantEntity> variants = productEntity.getVariants();

        int brandCount  = brandRepository.countBrandsByProductId(productId);

        if (brandCount > 1){
            productEntity.setAvailable(false);
            productRepository.save(productEntity);
            log.info("Product and its variants set as unavailable due to being linked with more than one brand. Product ID: {}", productId);
        }

        else{
            boolean variantsInCart = cartItemRepository.existsByVariantIn(variants);
            if (variantsInCart){

                for (ProductVariantEntity variant : variants){
                    variant.setAvailable(false);
                    variant.setDeleted(true);
                    variantRepository.save(variant);
                }

                productEntity.setAvailable(false);
                productRepository.save(productEntity);
                log.info("Product and its variants set as unavailable due to being in a user's cart. Product ID: {}", productId);
            }
            else {
                productEntity.setAvailable(false);
                productEntity.setDeleted(true);
                productRepository.save(productEntity);
                log.info("Product set as unavailable and deleted with id {}", productId);

                variants.forEach(variant -> {
                    variant.setAvailable(false);
                    variant.setDeleted(true);
                    variantRepository.save(variant);
                });
            }
        }
    }

    @Override
    public void deleteOrDisableProductVariantById(Integer variantId) {

        //variant in database base on variantId
        ProductVariantEntity variant = variantRepository.findById(variantId)
                .orElseThrow(() -> CustomExceptionHandler.notFoundException("Product variant's id not found"));

        ProductEntity productEntity = variant.getProduct();

        int quantityToDelete = variant.getQuantity() != null ? variant.getQuantity() : 0;

        productEntity.setTotalQuantityReceived(productEntity.getTotalQuantityReceived() - quantityToDelete);

        if (cartItemRepository.existsByVariant(variant)){
            variant.setAvailable(false);
            variant.setDeleted(true);
            variantRepository.save(variant);
            log.info("Product variant with id {} has been disabled and marked as deleted.", variantId);
        }

        else {
            variant.setAvailable(false);
            variant.setDeleted(true);
            variantRepository.save(variant);
            log.info("Product variant with id {} has been deleted.", variantId);
        }

    }

    @Override
    public ProductVariantResponse getProductVariantById(Integer variantId) {
        ProductVariantEntity productVariantEntity = variantRepository.findById(variantId)
                .orElseThrow(() -> CustomExceptionHandler.notFoundException("ProductVariant not found with id " + variantId));

        return ProductVariantResponse.builder()
                .variantId(productVariantEntity.getId())
                .size(SizeMapper.toSizeResponse(productVariantEntity.getSize()))
                .color(ColorMapper.toColorResponse(productVariantEntity.getColor()))
                .quantity(productVariantEntity.getQuantity())
                .isSoldOutVariant(productVariantEntity.isSoldOut())
                .build();
    }

    @Override
    public ProductResponse getProductById(Integer productId) {
        return ProductMapper.toProductDTOResponse(productRepository.findById(productId)
                .orElseThrow(() -> CustomExceptionHandler.notFoundException("Product not found with id" + productId)));
    }

    private String getUsernameFromSecurityContext(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    private ResponsePage<ProductResponse> convertProductPageToProductResponsePage(Page<ProductEntity> productPage){
        ResponsePage<ProductResponse> responsePage = new ResponsePage<>();
        responsePage.setContent(productPage.getContent().stream()
                .map(ProductMapper::toProductDTOResponse)
                .collect(Collectors.toList()));
        responsePage.setPageNumber(productPage.getNumber());
        responsePage.setPageSize(productPage.getSize());
        responsePage.setTotalElements(productPage.getTotalElements());
        responsePage.setTotalPages(productPage.getTotalPages());
        return responsePage;

    }

}
