package com.dao.shopping.dto.requests;


import com.dao.shopping.validator.ConfirmFieldConstraint;
import com.dao.shopping.validator.PasswordConstraint;
import com.dao.shopping.validator.UsernameConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@ConfirmFieldConstraint(
        password = "password",
        confirmPassword = "confirmPassword"
)
public class UserUpdateRequest {

    @UsernameConstraint
    String username;

    @PasswordConstraint
    String password;

    String firstName;
    String lastName;
    LocalDate dob;

    String confirmPassword;


}
