package com.linkedin.linkedin.exceptioncontroller;

public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException(String message){
        super(message);
    }
}
