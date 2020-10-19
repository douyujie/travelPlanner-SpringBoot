package com.example.springdemo.models.response;

import java.io.Serializable;

/**
 * response object that'll be returned to front-end
 * it wraps a responseObj, which can be an AttractionResult
 * or an AttractionDetail. Together with a responseCode and
 * a message(usually for error)
 */
@lombok.Data
public class BaseResponse<T> implements Serializable {
    protected String responseCode;
    protected T responseObj;
    protected String message;

    public BaseResponse(){}

    public BaseResponse(String responseCode, T responseObj, String message) {
        this.responseCode = responseCode;
        this.responseObj = responseObj;
        this.message = message;
    }
}