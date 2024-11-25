package com.dao.shopping.service.impl;

import com.dao.shopping.dto.requests.AuthenticationRequest;
import com.dao.shopping.dto.requests.RefreshTokenRequest;
import com.dao.shopping.dto.responses.AuthenticationResponse;
import com.dao.shopping.entity.RoleEntity;
import com.dao.shopping.entity.UserEntity;
import com.dao.shopping.enums.TokenType;
import com.dao.shopping.exception.CustomExceptionHandler;
import com.dao.shopping.repository.TokenRepository;
import com.dao.shopping.repository.UserRepository;
import com.dao.shopping.security.CustomUserDetailsService;
import com.dao.shopping.utils.JwtTokenUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private JwtTokenUtils jwtTokenUtils;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private TokenRepository tokenRepository;
    @Mock
    private CustomUserDetailsService customUserDetailsService;

    AuthenticationRequest request;
    UserEntity userEntity;
    UserDetails userDetails;
    RoleEntity roleEntity = new RoleEntity();
    AuthenticationResponse expected;

    @BeforeEach
    void setUp(){
        request = AuthenticationRequest.builder()
                .email("email")
                .password("password")
                .build();
        userEntity = new UserEntity();
        userEntity.setId(1);
        userEntity.setEmail("email");
        userEntity.setPassword("password");
        userEntity.setUsername("username");

        roleEntity.setName("USER");

        Set<RoleEntity> roleEntities = new HashSet<>();
        roleEntities.add(roleEntity);

        userEntity.setRoles(roleEntities);

        userDetails = new User(userEntity.getEmail(), userEntity.getPassword(),
                userEntity.getRoles().stream()
                        .map(r -> new SimpleGrantedAuthority(r.getName())).collect(Collectors.toSet()));

        expected = new AuthenticationResponse();
        expected.setAccessToken("access_token");
        expected.setRefreshToken("refresh_token");
        expected.setTokenType(TokenType.Bearer);
    }

    @Test
    void test_Authenticate_Successfully() {
        //given

        //when
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(userEntity));
        when(passwordEncoder.matches(request.getPassword(), userEntity.getPassword())).thenReturn(true);
        when(tokenRepository.findAllValidTokensByUserId((userEntity.getId()))).thenReturn(Collections.emptyList());
        when(customUserDetailsService.loadUserByUsername(userEntity.getEmail())).thenReturn(userDetails);
        when(jwtTokenUtils.generateAccessToken(userDetails)).thenReturn("access_token");
        when(jwtTokenUtils.generateRefreshToken(userDetails)).thenReturn("refresh_token");

        AuthenticationResponse actual = authenticationService.authenticate(request);

        //then
        assertNotNull(expected);
        Assertions.assertEquals(expected.getTokenType(), actual.getTokenType());
        Assertions.assertEquals(expected.getAccessToken(), actual.getAccessToken());
        Assertions.assertEquals(expected.getRefreshToken(), actual.getRefreshToken());

    }

    @Test
    void test_Authenticate_Failure_With_Email() {
        //given

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

        //then
        assertThrows(CustomExceptionHandler.class, () ->
            authenticationService.authenticate(request)
        );
    }

    @Test
    void test_Authenticate_Failure_With_Password() {

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(userEntity));
        when(passwordEncoder.matches(request.getPassword(), userEntity.getPassword())).thenReturn(false);

        CustomExceptionHandler exception = assertThrows(CustomExceptionHandler.class, () ->
           authenticationService.authenticate(request)
        );

        Assertions.assertEquals("Invalid password", exception.getError().getMessage());
    }

    @Test
    void refreshToken_Successfully() {
        //given
        RefreshTokenRequest refreshToken = new RefreshTokenRequest();
        refreshToken.setRefreshToken("refresh_token");

        String userEmail = "email";

        //when
        when(jwtTokenUtils.getUserEmailFromToken(refreshToken.getRefreshToken())).thenReturn(userEmail);
        when(customUserDetailsService.loadUserByUsername(userEmail)).thenReturn(userDetails);
        when(jwtTokenUtils.validate(refreshToken.getRefreshToken(), userDetails)).thenReturn(true);
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(userEntity));
        when(jwtTokenUtils.generateAccessToken(userDetails)).thenReturn("access_token");
        when(jwtTokenUtils.generateRefreshToken(userDetails)).thenReturn("refresh_token");

        //then
        AuthenticationResponse actual = authenticationService.refreshToken(refreshToken);

        assertNotNull(expected);
        Assertions.assertEquals(expected.getTokenType(), actual.getTokenType());
        Assertions.assertEquals(expected.getAccessToken(), actual.getAccessToken());
        Assertions.assertEquals(expected.getRefreshToken(), actual.getRefreshToken());

    }

    @Test
    void refreshToken_Failure_With_Invalid_Token() {
        RefreshTokenRequest refreshToken = new RefreshTokenRequest();
        refreshToken.setRefreshToken("refresh_token");

        String userEmail = "email";

        when(jwtTokenUtils.getUserEmailFromToken(refreshToken.getRefreshToken())).thenReturn(userEmail);
        when(customUserDetailsService.loadUserByUsername(userEmail)).thenReturn(userDetails);
        when(jwtTokenUtils.validate(refreshToken.getRefreshToken(), userDetails)).thenReturn(false);

        CustomExceptionHandler ex = assertThrows(CustomExceptionHandler.class, () ->
            authenticationService.refreshToken(refreshToken)
        );

        Assertions.assertEquals("Invalid or expired refresh token", ex.getError().getMessage());
    }

    @Test
    void refreshToken_Failure_With_Null_Token() {
        RefreshTokenRequest refreshToken = new RefreshTokenRequest();

        assertThrows(CustomExceptionHandler.class, () ->
            authenticationService.refreshToken(refreshToken)
        );

    }
}