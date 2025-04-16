package com.linkedin.linkedin.exceptioncontroller;


public class EmailNotVerifiedException extends RuntimeException{
    public EmailNotVerifiedException(String message){
        super(message);
    }
}
