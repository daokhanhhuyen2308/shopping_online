package com.dao.shopping.service.impl;

import com.dao.shopping.dto.requests.AuthenticationRequest;
import com.dao.shopping.dto.requests.RefreshTokenRequest;
import com.dao.shopping.dto.responses.AuthenticationResponse;
import com.dao.shopping.entity.RoleEntity;
import com.dao.shopping.entity.TokenEntity;
import com.dao.shopping.entity.UserEntity;
import com.dao.shopping.enums.TokenType;
import com.dao.shopping.exception.CustomExceptionHandler;
import com.dao.shopping.repository.TokenRepository;
import com.dao.shopping.repository.UserRepository;
import com.dao.shopping.security.CustomUserDetailsService;
import com.dao.shopping.service.IAuthenticationService;
import com.dao.shopping.utils.JwtTokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class AuthenticationServiceImpl implements IAuthenticationService {
    private final UserRepository userRepository;
    private final JwtTokenUtils jwtTokenUtils;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailsService customUserDetailsService;

    public AuthenticationServiceImpl(UserRepository userRepository, JwtTokenUtils jwtTokenUtils,
                                     TokenRepository tokenRepository, @Lazy PasswordEncoder passwordEncoder,
                                     @Lazy CustomUserDetailsService customUserDetailsService) {
        this.userRepository = userRepository;
        this.jwtTokenUtils = jwtTokenUtils;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        validateLoginRequest(request);

        var account = userRepository.findByEmail(request.getEmail());

        if (account.isEmpty()) {
            throw CustomExceptionHandler.notFoundException("User not found");
        }

        boolean isAuthenticated = passwordEncoder.matches(request.getPassword(), account.get().getPassword());

        if (!isAuthenticated) {
            throw CustomExceptionHandler.badRequestException("Invalid password");
        }

        return buildDTO(account.get());
    }

    @Override
    public AuthenticationResponse refreshToken(RefreshTokenRequest request) {

        if (request.getRefreshToken() == null){
            throw CustomExceptionHandler.badRequestException("Refresh token is required");
        }

        String userEmail = jwtTokenUtils.getUserEmailFromToken(request.getRefreshToken());

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(userEmail);

        if (!jwtTokenUtils.validate(request.getRefreshToken(), userDetails)){
            throw CustomExceptionHandler.unauthorizedException("Invalid or expired refresh token");
        }

        UserEntity userEntity = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> CustomExceptionHandler.notFoundException("User not found"));

        AuthenticationResponse authenticationResponse = buildDTO(userEntity);
        authenticationResponse.setRefreshToken(request.getRefreshToken());

        return authenticationResponse;
    }

    private void validateLoginRequest(AuthenticationRequest request) {

        if (request.getEmail() == null) {
            throw CustomExceptionHandler.badRequestException("Email is required");
        }

        if (request.getPassword() == null) {
            throw CustomExceptionHandler.badRequestException("Password is required");
        }
    }

    private AuthenticationResponse buildDTO(UserEntity userEntity){

        revokedAllAccountTokens(userEntity);
        log.info("Login successfully with role: {}", userEntity.getRoles().stream().findFirst().map(RoleEntity::getName).orElse(null));

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(userEntity.getEmail());

        String jwtToken = jwtTokenUtils.generateAccessToken(userDetails);
        String refreshToken = jwtTokenUtils.generateRefreshToken(userDetails);

        var token = TokenEntity.builder()
                .token(jwtToken)
                .user(userEntity)
                .tokenType(TokenType.Bearer)
                .isExpired(false)
                .isRevoke(false)
                .build();

        tokenRepository.save(token);

        return AuthenticationResponse.builder()
                .tokenType(TokenType.Bearer)
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void revokedAllAccountTokens(UserEntity userEntity){

        var validUserTokens = tokenRepository.findAllValidTokensByUserId(userEntity.getId());

        if (validUserTokens.isEmpty()){
            return;
        }

        validUserTokens.forEach(tokenEntity -> {
            tokenEntity.setRevoke(true);
            tokenEntity.setExpired(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

}
