package com.dao.shopping.service;


import com.dao.shopping.dto.requests.AuthenticationRequest;
import com.dao.shopping.dto.requests.RefreshTokenRequest;
import com.dao.shopping.dto.responses.AuthenticationResponse;

public interface IAuthenticationService {
    AuthenticationResponse authenticate(AuthenticationRequest request);

    AuthenticationResponse refreshToken(RefreshTokenRequest request);
}
