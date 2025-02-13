package com.tech.gateway.utils;
import org.springframework.http.HttpStatus;


public class ApiResponse<T> {    
private String status;
private HttpStatus statusCode;
private String message;
private T data;

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public HttpStatus getStatusCode() {
        return this.statusCode;
    }

    public void setStatusCode(HttpStatus status) {
        this.statusCode = status;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;

   
    }


public ApiResponse(String status,HttpStatus statusCode,String message,T data){
this.status=status;
this.statusCode=statusCode;
this.message=message;
this.data=data;
}



@Override
public String toString() {
    return "{" +
        " status='" + getStatus() + "'" +
        "Status Code=" + getStatusCode() +
        ", message='" + getMessage() + "'" +
        ", data='" + getData() + "'" +
        "}";
}





}
