package com.tech.gateway.exceptions;



record ErrorResponse(   int statusCode,
         String message,
         String details) {


    public ErrorResponse(int statusCode, String message, String details) {
        this.statusCode = statusCode;
        this.message = message;
        this.details = details;
    }


}