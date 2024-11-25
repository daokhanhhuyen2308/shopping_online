package com.dao.shopping.controller;

import com.dao.shopping.dto.requests.AuthenticationRequest;
import com.dao.shopping.dto.requests.RefreshTokenRequest;
import com.dao.shopping.dto.responses.ApiResponse;
import com.dao.shopping.dto.responses.AuthenticationResponse;
import com.dao.shopping.service.IAuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final IAuthenticationService iAuthenticationService;

    @PostMapping("/login")
    public ApiResponse<AuthenticationResponse> login(@Valid @RequestBody AuthenticationRequest request){
        ApiResponse<AuthenticationResponse> response = new ApiResponse<>();
        response.setMessage("Successfully logged in");
        response.setResult(iAuthenticationService.authenticate(request));
        return response;
    }

    @PostMapping("/refresh-token")
    public ApiResponse<AuthenticationResponse> refreshToken(@RequestBody RefreshTokenRequest request){
        ApiResponse<AuthenticationResponse> response = new ApiResponse<>();
        response.setMessage("Successfully refreshed token");
        response.setResult(iAuthenticationService.refreshToken(request));
        return response;
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(){
        return ResponseEntity.ok("Logged out successfully!");
    }

}
