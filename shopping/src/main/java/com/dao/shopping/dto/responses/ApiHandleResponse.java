package com.dao.shopping.dto.responses;


import lombok.Getter;

@Getter
public class ApiHandleResponse<T>{
    private int statusCode;
    private T error;

    public ApiHandleResponse(int statusCode, T error) {
        this.statusCode = statusCode;
        this.error = error;
    }

    public ApiHandleResponse(){

    }
    public boolean isSuccess() {
        return false;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public void setError(T error) {
        this.error = error;
    }
}

