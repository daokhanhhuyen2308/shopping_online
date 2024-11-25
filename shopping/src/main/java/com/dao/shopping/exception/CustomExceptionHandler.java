package com.dao.shopping.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomExceptionHandler extends RuntimeException{

    HttpStatus status;
    CustomError error;

    public CustomExceptionHandler() {
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public void setError(CustomError error) {
        this.error = error;
    }

    public static CustomExceptionHandler notFoundException(String message){

        CustomError customError = new CustomError();
        customError.setCode(HttpStatus.NOT_FOUND.name());
        customError.setMessage(message);
        customError.setDetails("The information that you provided for us could not be found. Please recheck your information!");

        CustomExceptionHandler exceptionHandler = new CustomExceptionHandler();
        exceptionHandler.setError(customError);
        exceptionHandler.setStatus(HttpStatus.NOT_FOUND);

        return exceptionHandler;

    }

    public static CustomExceptionHandler badRequestException(String message){

        CustomError customError = new CustomError();
        customError.setCode(HttpStatus.NOT_FOUND.name());
        customError.setMessage(message);
        customError.setDetails("The information that you provided for us could not be found. Please recheck your information!");

        CustomExceptionHandler exceptionHandler = new CustomExceptionHandler();
        exceptionHandler.setError(customError);
        exceptionHandler.setStatus(HttpStatus.BAD_REQUEST);

        return exceptionHandler;
    }

    public static CustomExceptionHandler unauthorizedException(String message){
        CustomError customError = new CustomError();
        customError.setCode(HttpStatus.UNAUTHORIZED.name());
        customError.setMessage(message);
        customError.setDetails("You must be logged");

        CustomExceptionHandler exceptionHandler = new CustomExceptionHandler();
        exceptionHandler.setError(customError);
        exceptionHandler.setStatus(HttpStatus.UNAUTHORIZED);

        return exceptionHandler;
    }



}
