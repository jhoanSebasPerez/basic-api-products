package com.example.basicapiproducts.controllers;

import com.example.basicapiproducts.dtos.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDate;
import java.util.NoSuchElementException;

@ControllerAdvice
public class ExceptionController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ExceptionResponse> handleNoSuchElementException(NoSuchElementException ex){
        ExceptionResponse response =
                new ExceptionResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage(), LocalDate.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}
