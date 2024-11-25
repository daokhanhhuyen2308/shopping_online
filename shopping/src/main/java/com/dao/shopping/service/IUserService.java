package com.dao.shopping.service;


import com.dao.shopping.dto.requests.UserCreationRequest;
import com.dao.shopping.dto.requests.UserUpdateRequest;
import com.dao.shopping.dto.responses.UserResponse;
import com.dao.shopping.entity.UserEntity;

import java.util.List;

public interface IUserService {
    UserResponse register(UserCreationRequest request);

    UserResponse getCurrentAccount();

    List<UserResponse> getAccountList();

    UserEntity getUserLoggedIn();

    UserResponse update(Integer id, UserUpdateRequest request);

    void deleteAccountById(int id);

}
