package com.dao.shopping.dto.requests;


import com.dao.shopping.validator.EmailConstraint;
import com.dao.shopping.validator.PasswordConstraint;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthenticationRequest {

    @EmailConstraint
    private String email;

    @PasswordConstraint
    private String password;
}
