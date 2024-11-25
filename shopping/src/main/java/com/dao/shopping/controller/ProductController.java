package com.dao.shopping.controller;


import com.dao.shopping.dto.requests.ProductCreationRequest;
import com.dao.shopping.dto.requests.ProductFilterRequest;
import com.dao.shopping.dto.requests.ProductUpdateRequest;
import com.dao.shopping.dto.responses.ApiResponse;
import com.dao.shopping.dto.responses.ProductResponse;
import com.dao.shopping.dto.responses.ProductVariantResponse;
import com.dao.shopping.dto.responses.ResponsePage;
import com.dao.shopping.service.IProductService;
import com.dao.shopping.validator.Admin;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

    private final IProductService iProductService;

    @PostMapping("/add")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ApiResponse<ProductResponse> createProduct(@RequestBody ProductCreationRequest add){
        ApiResponse<ProductResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully created product");
        apiResponse.setResult(iProductService.createProduct(add));
        return apiResponse;
    }

    @GetMapping("/{productId}")
    public ApiResponse<ProductResponse> getProductById(@PathVariable Integer productId){
        return ApiResponse.<ProductResponse>builder()
                .result(iProductService.getProductById(productId))
                .build();
    }

    @GetMapping("/variants/{variantId}")
    public ApiResponse<ProductVariantResponse> getProductVariantById(@PathVariable Integer variantId){
        return ApiResponse.<ProductVariantResponse>builder()
               .result(iProductService.getProductVariantById(variantId))
               .build();
    }

    @GetMapping("/all-products")
    public ApiResponse<ResponsePage<ProductResponse>> getAll(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "brandName", required = false) String brandName,
            @RequestParam(name = "category", required = false) String category,
            @RequestParam(name = "color", required = false) String color,
            @RequestParam(name = "minPrice", required = false) Double minPrice,
            @RequestParam(name = "maxPrice", required = false) Double maxPrice,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "5") Integer size){

        ProductFilterRequest filter = ProductFilterRequest.builder().name(name).brandName(brandName)
                .minPrice(minPrice).maxPrice(maxPrice)
                .category(category).color(color).page(page).size(size).build();

        ApiResponse<ResponsePage<ProductResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully retrieved products by filter function");
        apiResponse.setResult(iProductService.getAllProduct(filter));
        return apiResponse;
    }

    @GetMapping("/{slug}")
    public ApiResponse<ProductResponse> getProductBySlug(@PathVariable String slug){
        return ApiResponse.<ProductResponse>builder()
               .result(iProductService.getProductBySlug(slug))
               .build();
    }

    @GetMapping("/sold-out")
    public ResponseEntity<ResponsePage<ProductResponse>> getSoldOut(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                          @RequestParam(name = "size", defaultValue = "5") Integer size){

        return ResponseEntity.ok(iProductService.getSoldOut(page, size));
    }

    @GetMapping("/best-selling")
    public ResponseEntity<ResponsePage<ProductResponse>> getBestSelling(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                                       @RequestParam(name = "size", defaultValue = "5") Integer size){

        return ResponseEntity.ok(iProductService.getBestSelling(page, size));
    }

    @PutMapping("/update/{id}")
    @Admin
    public ApiResponse<ProductResponse> updateProduct(@PathVariable Integer id, @RequestBody ProductUpdateRequest request){
        ApiResponse<ProductResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully updated product");
        apiResponse.setResult(iProductService.updateProductById(id, request));
        return apiResponse;
    }

    @DeleteMapping("/{productId}")
    @Admin
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer productId){
        iProductService.deleteProductById(productId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/variant/{variantId}")
    @Admin
    public ResponseEntity<Void> deleteProductVariant(@PathVariable Integer variantId){
        iProductService.deleteOrDisableProductVariantById(variantId);
        return ResponseEntity.noContent().build();
    }


}
