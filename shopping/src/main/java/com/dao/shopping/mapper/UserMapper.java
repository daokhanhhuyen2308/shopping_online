package com.dao.shopping.mapper;

import com.dao.shopping.dto.requests.UserCreationRequest;
import com.dao.shopping.dto.requests.UserUpdateRequest;
import com.dao.shopping.dto.responses.UserResponse;
import com.dao.shopping.entity.RoleEntity;
import com.dao.shopping.entity.UserEntity;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

public class UserMapper {

    public static UserResponse toAccountDTOResponse(UserEntity userEntity){
        UserResponse response = new UserResponse();
        response.setId(userEntity.getId());
        response.setUsername(userEntity.getUsername());
        response.setEmail(userEntity.getEmail());
        response.setRoles(userEntity.getRoles().stream().map(RoleEntity::getName).collect(Collectors.toSet()));
        response.setAddresses(userEntity.getAddresses().stream().map(AddressMapper::mapAddressToAddressResponse).toList());
        response.setCreatedBy(userEntity.getCreatedBy());
        response.setCreatedDate(userEntity.getCreatedDate());
        response.setLastModifiedBy(userEntity.getLastModifiedBy());
        response.setLastModifiedDate(userEntity.getLastModifiedDate());
        response.setDeleted(userEntity.getDeleted());
        return response;
    }

    public static UserEntity toAccount(UserCreationRequest register){
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(register.getUsername());
        userEntity.setEmail(register.getEmail());
        userEntity.setPassword(register.getPassword());
        userEntity.setDob(register.getDob());
        userEntity.setFirstName(register.getFirstName());
        userEntity.setLastName(register.getLastName());
        return userEntity;
    }

    public static UserEntity toUpdateAccount(Integer id, UserUpdateRequest request){
        UserEntity userEntity = new UserEntity();
        userEntity.setId(id);
        userEntity.setUsername(request.getUsername());
        userEntity.setLastModifiedDate(LocalDateTime.now());
        userEntity.setLastModifiedBy(request.getUsername());
        userEntity.setDob(request.getDob());
        userEntity.setFirstName(request.getFirstName());
        userEntity.setLastName(request.getLastName());
        return userEntity;
    }

}
