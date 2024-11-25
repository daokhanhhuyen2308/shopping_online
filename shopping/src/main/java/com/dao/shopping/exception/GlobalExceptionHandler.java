package com.dao.shopping.exception;

import com.dao.shopping.dto.responses.ApiHandleResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;
import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomExceptionHandler.class)
    public ResponseEntity<ApiHandleResponse<CustomError>> handleProductException(CustomExceptionHandler ex, HttpServletRequest request){

        ApiHandleResponse<CustomError> apiHandleResponse = new ApiHandleResponse<>();
        apiHandleResponse.setStatusCode(ex.getStatus().value());

        CustomError customError = ex.getError();
        customError.setPath(request.getRequestURI());

        return ResponseEntity.status(ex.getStatus()).body(apiHandleResponse);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiHandleResponse<CustomError>> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException exception,
                                                                           HttpServletRequest request){

        ApiHandleResponse<CustomError> apiHandleResponse = new ApiHandleResponse<>();
        apiHandleResponse.setStatusCode(HttpServletResponse.SC_METHOD_NOT_ALLOWED);

        CustomError customError = new CustomError();
        customError.setCode("METHOD_NOT_ALLOWED");
        customError.setPath(request.getRequestURI());
        customError.setMessage("The " +exception.getMethod() + " method is not supported for route " + request.getRequestURI()
                + ". Supported method: " + exception.getSupportedHttpMethods());
        customError.setDetails("The request method " + exception.getSupportedHttpMethods() + " is not supported. Please recheck!");
        customError.setTimestamp(new Date());
        customError.setDetails(request.getRequestURI());

        apiHandleResponse.setError(customError);

        return new ResponseEntity<>(apiHandleResponse, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomError> handleGeneralException(Exception exception, HttpServletRequest request){

        CustomError customError = new CustomError();
                customError.setCode("INTERNAL_SERVER_ERROR");
                customError.setDetails("An unexpected error occurred. Please try again later!");
                customError.setMessage(exception.getMessage());
                customError.setTimestamp(new Date());
                customError.setPath(request.getRequestURI());

        return new ResponseEntity<>(customError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ApiHandleResponse<CustomError>> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception,
                                                                                                HttpServletRequest request){
        ApiHandleResponse<CustomError> apiHandleResponse = new ApiHandleResponse<>();
        apiHandleResponse.setStatusCode(HttpServletResponse.SC_BAD_REQUEST);

        CustomError error = new CustomError();
        error.setCode("VALIDATION_ERROR");
        error.setPath(request.getRequestURI());
        error.setDetails("Validation failed. Please check your input.");
        error.setTimestamp(new Date());
        error.setMessage(Objects.requireNonNull(exception.getBindingResult().getFieldError()).getDefaultMessage());

        apiHandleResponse.setError(error);
        return ResponseEntity.badRequest().body(apiHandleResponse);
    }

}
