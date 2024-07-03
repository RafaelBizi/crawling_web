package com.axreng.backend.application.response;

import com.google.gson.annotations.SerializedName;

public class ErrorResponse {

    @SerializedName(value = "status", alternate = "statusCode")
    protected Integer statusCode;

    @SerializedName(value = "errorMessage", alternate = "message")
    private String message;

    public ErrorResponse(Integer statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
}
