package com.linkedin.linkedin.exceptioncontroller;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Map;

@ControllerAdvice
@RestController
public class BackendExceptionController {

    //body messing


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String,String>> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return ResponseEntity.badRequest().body(Map.of("message","required body is missing"));
    }
//        "message": "password: Password is require; email: Email is require; "

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        StringBuilder errorMessage = new StringBuilder();
        e.getBindingResult().getFieldErrors().forEach(error ->
                errorMessage.append(error.getField()).append(": ").append(error.getDefaultMessage()).append("; ")
        );
        return ResponseEntity.badRequest().body(Map.of("message", errorMessage.toString()));
    }
//resource not found
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Map<String, String>> handleNoResourceFoundException(NoResourceFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
    }
//duplicate
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        if (e.getMessage().contains("Duplicate entry")) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email already exists, please use another email or login."));
        }
        return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
    }

    //error 500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", e.getMessage()));
    }

    @ExceptionHandler(EmailNotVerifiedException.class)
    public ProblemDetail EmailNotVerifiedException(EmailNotVerifiedException ex){
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problemDetail.setTitle(ex.getMessage());
        problemDetail.setDetail(ex.getLocalizedMessage());
        return problemDetail;
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ProblemDetail InvalidCredentialsException(InvalidCredentialsException ex){
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problemDetail.setTitle(ex.getMessage());
        problemDetail.setDetail(ex.getLocalizedMessage());
        return problemDetail;
    }
}
