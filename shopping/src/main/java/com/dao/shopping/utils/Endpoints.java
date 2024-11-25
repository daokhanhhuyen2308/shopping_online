package com.dao.shopping.utils;

public class Endpoints {

    public static final String[] PUBLIC = {
            "/api/auth/login",
            "/api/auth/refresh-token",
            "/api/auth/logout",
            "/user/sign-up",
            "/product/all-products",
            "/product/sold-out",
            "/product/best-selling",
            "/size/all-sizes",
            "/color/all-colors",
            "/brand/all-brands",
            "/category/all-categories",
            "/product/{productId}",
            "/variant/{variantId}",
            "/sale/{/type}"

    };

    public static final String[] SWAGGER_UI = {
            "/api-docs/**",
            "v2/api-docs",
            "v3/api-docs",
            "swagger-resources",
            "swagger-resources/**",
            "/swagger-ui.html",
            "/swagger-ui/**",
    };

    public static final String[] USER = {
            "/cart/add",
            "/cart/all-cart-items",
            "/user/account",
            "/user/update/{id}",
            "/cart/update/{cartItemId}",
            "/cart/delete/{cartItemId}"
    };

    public static final String[] ADMIN = {
            "/api/**",
            "/product/**",
            "/cart/**",
            "/size/**",
            "/color/**",
            "/brand/**",
            "/gift/**",
            "/voucher/**",
            "/category/**",
            "/order/**",
            "/sale/**"
    };

}
