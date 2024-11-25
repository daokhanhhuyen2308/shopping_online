package com.dao.shopping.validator;

import com.dao.shopping.exception.CustomExceptionHandler;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;

public class ConfirmFieldValidator implements ConstraintValidator<ConfirmFieldConstraint, Object> {

    private String password;
    private String confirmPassword;
    private String message;

    @Override
    public void initialize(ConfirmFieldConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        this.password = constraintAnnotation.password();
        this.confirmPassword = constraintAnnotation.confirmPassword();
        this.message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        BeanWrapperImpl wrapper = new BeanWrapperImpl(value);

        Object fieldValue = wrapper.getPropertyValue(this.password);
        Object confirmValue = wrapper.getPropertyValue(this.confirmPassword);

        if (fieldValue == null) {
            throw CustomExceptionHandler.badRequestException("Please enter your password");
        }

        if (confirmValue == null) {
            throw CustomExceptionHandler.badRequestException("Please confirm your password");
        }

        boolean isValid = fieldValue.equals(confirmValue);

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message)
                    .addConstraintViolation();
        }

        return isValid;
    }
}
