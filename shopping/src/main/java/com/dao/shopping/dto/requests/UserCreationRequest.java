package com.dao.shopping.dto.requests;


import com.dao.shopping.validator.ConfirmFieldConstraint;
import com.dao.shopping.validator.EmailConstraint;
import com.dao.shopping.validator.PasswordConstraint;
import com.dao.shopping.validator.UsernameConstraint;

import java.time.LocalDate;

@ConfirmFieldConstraint(
        password = "password",
        confirmPassword = "confirmPassword"
)
public class UserCreationRequest {

    @UsernameConstraint
    private String username;

    private String firstName;
    private String lastName;
    private LocalDate dob;

    @EmailConstraint
    private String email;

    @PasswordConstraint
    private String password;

    private String confirmPassword;

    private String houseNumber;
    private String ward;
    private String district;
    private String city;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
