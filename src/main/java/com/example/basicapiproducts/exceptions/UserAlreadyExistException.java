package com.example.basicapiproducts.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UserAlreadyExistException extends ResponseStatusException {

    public UserAlreadyExistException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
