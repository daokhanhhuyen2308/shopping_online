package com.dao.shopping.service.impl;


import com.dao.shopping.constant.DefaultRoles;
import com.dao.shopping.dto.requests.UserCreationRequest;
import com.dao.shopping.dto.responses.UserResponse;
import com.dao.shopping.entity.CartEntity;
import com.dao.shopping.entity.RoleEntity;
import com.dao.shopping.entity.UserEntity;
import com.dao.shopping.exception.CustomExceptionHandler;
import com.dao.shopping.repository.CartRepository;
import com.dao.shopping.repository.RoleRepository;
import com.dao.shopping.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    public UserServiceImpl accountService;

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    RoleRepository roleRepository;

    @Mock
    CartRepository cartRepository;

    UserCreationRequest request;
    UserEntity userEntity;
    LocalDateTime fixedTime;
    RoleEntity roleEntity = new RoleEntity();
    UserResponse expected;

    @BeforeEach
    void setUp() {

        fixedTime = LocalDateTime.of(2024, 6, 4, 7, 25, 48);

        request = new UserCreationRequest();
        request.setUsername("username");
        request.setEmail("email@gmail.com");
        request.setPassword("password");
        request.setConfirmPassword("password");

        userEntity = new UserEntity();
        userEntity.setId(1);
        userEntity.setUsername("username");
        userEntity.setPassword("password");
        userEntity.setEmail("email@gmail.com");
        userEntity.setRoles(new HashSet<>());
        userEntity.setCreatedDate(fixedTime);
        userEntity.setLastModifiedDate(fixedTime);
        userEntity.setFirstName("firstName");
        userEntity.setLastName("lastName");
        userEntity.setDob(LocalDate.of(1990, 1, 1));

        roleEntity.setName("USER");

        Set<RoleEntity> roleEntities = new HashSet<>();
        roleEntities.add(roleEntity);

        userEntity.setRoles(roleEntities);

        expected = UserResponse.builder()
                .id(1).username("username")
                .email("email@gmail.com")
                .firstName("firstName")
                .lastName("lastName")
                .dob(LocalDate.of(1990, 1, 1))
                .deleted(false)
                .createdDate(fixedTime)
                .lastModifiedDate(fixedTime)
                .createdBy("username")
                .lastModifiedBy("username")
                .build();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void test_Register_Success() {

        //given
        CartEntity cartEntity = new CartEntity();
        cartEntity.setUser(userEntity);
        cartEntity.setId(1);

        userEntity.setCart(cartEntity);

        Set<String> roles = new HashSet<>();
        roles.add(DefaultRoles.USER_ROLE);
        expected.setRoles(roles);

        //when
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(userRepository.findByUsername(request.getUsername())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(request.getPassword())).thenReturn("password");
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
        when(cartRepository.save(any(CartEntity.class))).thenReturn(cartEntity);

        when(roleRepository.findById(DefaultRoles.USER_ROLE)).thenReturn(Optional.of(roleEntity));


        UserResponse actual = accountService.register(request);

        //then
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
        assertEquals(expected.getId(), actual.getId());

    }

    @Test
    void test_Register_Failure_NullUsername(){
        //given

        UserCreationRequest register = new UserCreationRequest();
                register.setEmail("email@gmail.com");
                register.setPassword("password");
                register.setConfirmPassword("password");

        //when

        CustomExceptionHandler ex = assertThrows(CustomExceptionHandler.class, () ->
            accountService.register(register));

        //then

        assertEquals("Username is required", ex.getError().getMessage());

    }

    @Test
    void test_Register_Failure_NullEmail(){
        //given
        UserCreationRequest register = new UserCreationRequest();
        register.setUsername("username");
        register.setPassword("password");
        register.setConfirmPassword("password");

        //when
        CustomExceptionHandler ex = assertThrows(CustomExceptionHandler.class, () ->
            accountService.register(register)
        );

        //then

        assertEquals("Email is required", ex.getError().getMessage());
    }

    @Test
    void test_Register_Failure_NullPassword(){

        //given
        UserCreationRequest register = new UserCreationRequest();
        register.setEmail("email@gmail.com");
        register.setUsername("username");

        //when

        CustomExceptionHandler ex = assertThrows(CustomExceptionHandler.class, () ->
            accountService.register(register)
        );

        //then

        assertEquals("Password is required", ex.getError().getMessage());
    }

    @Test
    void test_Register_Failure_InvalidUsername(){
        //given
        UserCreationRequest register = new UserCreationRequest();
        register.setUsername("us");
        register.setEmail("email@gmail.com");
        register.setPassword("password");
        register.setConfirmPassword("password");

        //when
        CustomExceptionHandler ex = assertThrows(CustomExceptionHandler.class, () ->
            accountService.register(register)
        );

        //then
        assertEquals("Invalid username", ex.getError().getMessage());

    }


    @Test
    void test_Register_Failure_InvalidPassword(){

        //given
        UserCreationRequest register = new UserCreationRequest();
        register.setUsername("username");
        register.setEmail("email@gmail.com");
        register.setPassword("pw");

        //when
        MethodArgumentNotValidException ex = assertThrows(MethodArgumentNotValidException.class, () ->
            accountService.register(register)
        );

        //then
        assertEquals("Invalid password", ex.getMessage());
    }

    @Test
    void test_Register_Failure_MismatchedPassword(){
        //given
        UserCreationRequest register = new UserCreationRequest();
        register.setUsername("username");
        register.setEmail("email@gmail.com");
        register.setPassword("password");
        register.setConfirmPassword("wrong");

        //when

        MethodArgumentNotValidException ex = assertThrows(MethodArgumentNotValidException.class, () ->
            accountService.register(register)
        );

        //then
        assertTrue(ex.getBindingResult().hasErrors());
        String errorMessage = Objects.requireNonNull(ex.getBindingResult().getFieldError("confirmPassword")).getDefaultMessage();
        assertEquals("Invalid confirm password. Please enter your password information!", errorMessage);
    }

    @Test
    void test_Register_Failure_InvalidEmail(){
        UserCreationRequest register = new UserCreationRequest();
        register.setUsername("username");
        register.setEmail("em");
        register.setPassword("password");
        register.setConfirmPassword("password");

        //when

        CustomExceptionHandler ex = assertThrows(CustomExceptionHandler.class, () ->
            accountService.register(register)
        );

        //then
        assertEquals("Invalid email", ex.getError().getMessage());
    }

    @Test
    void test_Register_Failure_ExistedEmail(){
        //given

        UserCreationRequest register = new UserCreationRequest();
        register.setUsername("username");
        register.setEmail("email@gmail.com");
        register.setPassword("password");

        UserEntity userEntity = new UserEntity();
        userEntity.setId(1);
        userEntity.setEmail("email@gmail.com");
        userEntity.setUsername("user");
        userEntity.setPassword("password");

        //when
        when(userRepository.findByEmail(register.getEmail())).thenReturn(Optional.of(userEntity));

        CustomExceptionHandler ex = assertThrows(CustomExceptionHandler.class, () ->

            accountService.register(register)
                );

        //then
        assertEquals("Email already in use", ex.getError().getMessage());



    }

    @Test
    void test_Register_Failure_ExistedUsername(){

        //given

        UserCreationRequest register = new UserCreationRequest();
        register.setUsername("username");
        register.setEmail("email@gmail.com");
        register.setPassword("password");

        UserEntity userEntity = new UserEntity();
        userEntity.setId(1);
        userEntity.setEmail("email1@gmail.com");
        userEntity.setUsername("username");
        userEntity.setPassword("password");

        //when
        when(userRepository.findByUsername(register.getUsername())).thenReturn(Optional.of(userEntity));

        CustomExceptionHandler ex = assertThrows(CustomExceptionHandler.class, () ->
            accountService.register(register)
        );

        //then
        assertEquals("Username already in use", ex.getError().getMessage());
    }

    @Test
    void test_GetCurrentAccount_Success() {

    }

    @Test
    void test_GetCurrentAccount_Failure() {
    }

    @Test
    void test_GetUserLoggedIn_Success() {
    }

    @Test
    void test_Update_Success() {

    }

    @Test
    void test_Update_Failure_NotMatchId(){

    }

    @Test
    void test_GetAccountList_Success() {

        List<UserEntity> entityList = List.of(userEntity);

        when(userRepository.findAll()).thenReturn(entityList);

        List<UserResponse> actual = accountService.getAccountList();

        List<UserResponse> expected =  List.of(UserResponse.builder().id(1).username("username").email("email@gmail.com").build());

        for (int i = 0; i < actual.size(); i++) {
            UserResponse response = expected.get(i);
            UserResponse actualAccount = actual.get(i);
            assertEquals(response.getId(), actualAccount.getId());
            assertEquals(response.getEmail(), actualAccount.getEmail());
            assertEquals(response.getUsername(), actualAccount.getUsername());
        }



    }
}