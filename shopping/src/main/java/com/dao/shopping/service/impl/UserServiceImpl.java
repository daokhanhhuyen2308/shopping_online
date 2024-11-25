package com.dao.shopping.service.impl;


import com.dao.shopping.constant.DefaultRoles;
import com.dao.shopping.dto.requests.UserCreationRequest;
import com.dao.shopping.dto.requests.UserUpdateRequest;
import com.dao.shopping.dto.responses.UserResponse;
import com.dao.shopping.entity.CartEntity;
import com.dao.shopping.entity.RoleEntity;
import com.dao.shopping.entity.UserEntity;
import com.dao.shopping.exception.CustomExceptionHandler;
import com.dao.shopping.mapper.UserMapper;
import com.dao.shopping.repository.CartRepository;
import com.dao.shopping.repository.RoleRepository;
import com.dao.shopping.repository.UserRepository;
import com.dao.shopping.service.IUserService;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserServiceImpl implements IUserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final CartRepository cartRepository;

    @Override
    @Transactional
    public UserResponse register(UserCreationRequest request) {

        UserEntity userEntity = validateRegisterRequest(request);

        Set<RoleEntity> authorities = new HashSet<>();

        authorities.add(roleRepository.findById(DefaultRoles.USER_ROLE).orElseThrow(() ->
                CustomExceptionHandler.notFoundException("Default role not found")));

        userEntity.setRoles(authorities);
        userEntity = userRepository.save(userEntity);

        return UserMapper.toAccountDTOResponse(userEntity);
    }


    @Override
    public UserResponse getCurrentAccount() {
        UserEntity userEntity = getUserLoggedIn();

        if (Objects.isNull(userEntity)) {
            throw CustomExceptionHandler.notFoundException("User is not logged in");
        }

        return UserMapper.toAccountDTOResponse(userEntity);
    }

    @Override
    public UserEntity getUserLoggedIn() {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails){
            String email = ((UserDetails) principal).getUsername();
            Optional<UserEntity> optionalAccount = userRepository.findByEmail(email);
            if (optionalAccount.isPresent()){
                return optionalAccount.get();
            }
        }
        return null;
    }

    @Override
    public UserResponse update(Integer id, UserUpdateRequest request) {

        UserEntity userEntityLoggedIn = getUserLoggedIn();

        if (userEntityLoggedIn == null) {
            throw CustomExceptionHandler.notFoundException("User is not logged in");
        }

        if (!Objects.equals(userEntityLoggedIn.getId(), id)) {
            throw CustomExceptionHandler.badRequestException("Account id mismatch");
        }

        boolean usernameExist = userRepository.findByUsername(request.getUsername()).isPresent();

        if (usernameExist){
            throw CustomExceptionHandler.badRequestException("Username already in use");
        }

        userEntityLoggedIn = UserMapper.toUpdateAccount(id, request);
        userEntityLoggedIn.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(userEntityLoggedIn);

        return UserMapper.toAccountDTOResponse(userEntityLoggedIn);
    }

    @Override
    public List<UserResponse> getAccountList() {

        List<UserEntity> entityList = userRepository.findAll();

        return entityList.stream().map(UserMapper::toAccountDTOResponse).toList();
    }

    @Override
    public void deleteAccountById(int id) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> CustomExceptionHandler.notFoundException("Account not found by id"));
        userRepository.delete(userEntity);
    }

    private UserEntity validateRegisterRequest(UserCreationRequest request){

        String username = request.getUsername();
        String email = request.getEmail();
        String password = request.getPassword();

        boolean usernameExist = userRepository.findByUsername(username).isPresent();
        boolean emailExist = userRepository.findByEmail(email).isPresent();

        if (StringUtils.isEmpty(username)) {
            throw CustomExceptionHandler.badRequestException("Username is required");
        }

        if (StringUtils.isEmpty(email)) {
            throw CustomExceptionHandler.badRequestException("Email is required");
        }

        if (StringUtils.isEmpty(password)) {
            throw CustomExceptionHandler.badRequestException("Password is required");
        }

        if (emailExist) {
            throw CustomExceptionHandler.badRequestException("Email already in use");
        }

        if (usernameExist){
            throw CustomExceptionHandler.badRequestException("Username already in use");
        }

        UserEntity userEntity = UserMapper.toAccount(request);

        userEntity.setPassword(passwordEncoder.encode(request.getPassword()));
        userEntity.setCreatedDate(LocalDateTime.now());
        userEntity.setCreatedBy(request.getUsername());
        userEntity.setLastModifiedDate(LocalDateTime.now());
        userEntity.setLastModifiedBy(request.getUsername());

        userEntity = userRepository.save(userEntity);

        CartEntity cartEntity = new CartEntity();
        cartEntity.setUser(userEntity);

        cartRepository.save(cartEntity);

        userEntity.setCart(cartEntity);

        return userEntity;
    }



}
