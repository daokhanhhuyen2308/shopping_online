package com.dao.shopping.service;


import com.dao.shopping.dto.requests.AddToCartRequest;
import com.dao.shopping.dto.requests.CartItemUpdateRequest;
import com.dao.shopping.dto.responses.CartItemResponse;
import com.dao.shopping.dto.responses.CartResponse;

import java.util.Map;

public interface ICartService {

    CartResponse addToCart(AddToCartRequest request, Integer page, Integer size);

    Map<String, Object> getCartItems(Integer page, Integer size);

    CartItemResponse updateCartItem(Integer cartItemId, CartItemUpdateRequest update);

    CartResponse deleteCartItem(Integer cartItemId, Integer page, Integer size);

}
