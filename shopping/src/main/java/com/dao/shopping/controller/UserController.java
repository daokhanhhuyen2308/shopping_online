package com.dao.shopping.controller;


import com.dao.shopping.dto.requests.UserCreationRequest;
import com.dao.shopping.dto.requests.UserUpdateRequest;
import com.dao.shopping.dto.responses.ApiResponse;
import com.dao.shopping.dto.responses.UserResponse;
import com.dao.shopping.service.IUserService;
import com.dao.shopping.validator.Admin;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final IUserService iUserService;

    @PostMapping("/sign-up")
    public ApiResponse<UserResponse> register(@Valid @RequestBody UserCreationRequest request){
        ApiResponse<UserResponse> response = new ApiResponse<>();
        response.setMessage("Successfully registered");
        response.setResult(iUserService.register(request));
        return response;
    }

    @GetMapping("/account")
    public ApiResponse<UserResponse> getCurrentAccount(){
        ApiResponse<UserResponse> response = new ApiResponse<>();
        response.setMessage("Successfully logged in");
        response.setResult(iUserService.getCurrentAccount());
        return response;
    }

    @PutMapping("/update/{id}")
    public ApiResponse<UserResponse> update(@PathVariable int id, @Valid @RequestBody UserUpdateRequest request){
        ApiResponse<UserResponse> response = new ApiResponse<>();
        response.setMessage("Successfully logged in");
        response.setResult(iUserService.update(id, request));
        return response;
    }

    @GetMapping("/all-users")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ApiResponse<List<UserResponse>> getAccountList(){
        ApiResponse<List<UserResponse>> response = new ApiResponse<>();
        response.setMessage("Successfully received the list accounts");
        response.setResult(iUserService.getAccountList());
        return response;
    }

    @DeleteMapping("/delete/{id}")
    @Admin
    public ApiResponse<String> deleteAccountById(@PathVariable int id){
        ApiResponse<String> response = new ApiResponse<>();
        response.setMessage("Successfully deleted account by id: " +id);
        iUserService.deleteAccountById(id);
        return response;
    }


}
