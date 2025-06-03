package com.example.demo.dto;

import lombok.Data;

@Data
public class BaseApiResponse<T> {
    private boolean success;
    private T body;
    private String errMsg;

    public BaseApiResponse() {
    };

    public BaseApiResponse(T body) {
        this.success = true;
        this.body = body;
        this.errMsg = " ";
    };

    public BaseApiResponse(String errMsg) {
        this.success = false;
        this.body = null;
        this.errMsg = errMsg;
    };

}
