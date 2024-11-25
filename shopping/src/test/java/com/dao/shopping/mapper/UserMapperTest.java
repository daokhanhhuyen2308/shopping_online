package com.dao.shopping.mapper;


import com.dao.shopping.dto.requests.UserCreationRequest;
import com.dao.shopping.dto.responses.UserResponse;
import com.dao.shopping.entity.CartEntity;
import com.dao.shopping.entity.OrderEntity;
import com.dao.shopping.entity.UserEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class UserMapperTest {


    @Test
    public void test_toAccountDTOResponse(){

        //given
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1);
        userEntity.setUsername("username");
        userEntity.setPassword("password");
        userEntity.setEmail("email");
        userEntity.setRoles(new HashSet<>());
        userEntity.setCreatedBy("user");
        userEntity.setCreatedDate(LocalDateTime.now());
        userEntity.setLastModifiedBy("user");
        userEntity.setLastModifiedDate(LocalDateTime.now());
        userEntity.setCart(new CartEntity());
        userEntity.setOrders(Arrays.asList(new OrderEntity(), new OrderEntity()));

        //when
        UserResponse expected = UserResponse.builder().id(1).username("username")
                .email("email").roles(new HashSet<>()).build();

        UserResponse actual = UserMapper.toAccountDTOResponse(userEntity);

        //then
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getUsername(), actual.getUsername());
        assertEquals(expected.getEmail(), actual.getEmail());


    }

    @Test
    public void test_toAccount(){

        //given
        UserCreationRequest register = new UserCreationRequest();
        register.setUsername("username");
        register.setEmail("email");
        register.setPassword("password");
        register.setConfirmPassword("password");

        //when

        UserEntity expected = new UserEntity();
        expected.setUsername("username");
        expected.setPassword("password");
        expected.setEmail("email");

        UserEntity actual = UserMapper.toAccount(register);

        //then

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);

    }

}
