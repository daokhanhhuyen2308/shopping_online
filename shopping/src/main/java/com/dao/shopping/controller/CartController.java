package com.dao.shopping.controller;

import com.dao.shopping.dto.requests.AddToCartRequest;
import com.dao.shopping.dto.requests.CartItemUpdateRequest;
import com.dao.shopping.dto.responses.ApiResponse;
import com.dao.shopping.dto.responses.CartItemResponse;
import com.dao.shopping.dto.responses.CartResponse;
import com.dao.shopping.service.ICartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {

    private final ICartService iCartService;

    @PostMapping("/add")
    public ResponseEntity<CartResponse> addToCart(@RequestBody AddToCartRequest request,
                                                  @RequestParam(name = "page", defaultValue = "1") Integer page,
                                                  @RequestParam(name = "size", defaultValue = "10") Integer size){
        return ResponseEntity.ok(iCartService.addToCart(request, page, size));
    }

    @GetMapping("/all-cart-items")
    public Map<String, Object> getAllCartItems(@RequestParam(name = "page", defaultValue = "1") Integer page,
                                               @RequestParam(name = "size", defaultValue = "10") Integer size){
        return iCartService.getCartItems(page, size);
    }

    @PutMapping("/update/{cartItemId}")
    public ApiResponse<CartItemResponse> updateCart(@PathVariable Integer cartItemId, @RequestBody CartItemUpdateRequest update){
        ApiResponse<CartItemResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully updated cart");
        apiResponse.setResult(iCartService.updateCartItem(cartItemId, update));
        return apiResponse;
    }

    @DeleteMapping("/delete/{cartItemId}")
    public CartResponse deleteCart(@PathVariable Integer cartItemId,
                                   @RequestParam(name = "page", defaultValue = "1") Integer page,
                                   @RequestParam(name = "size", defaultValue = "10") Integer size){
       return iCartService.deleteCartItem(cartItemId, page, size);

    }

}
