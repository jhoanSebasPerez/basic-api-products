package com.example.basicapiproducts.dtos;

import org.springframework.http.HttpStatus;

import java.time.LocalDate;

public class ExceptionResponse {

    private int status;
    private String error;
    private LocalDate date;

    public ExceptionResponse(int status, String message, LocalDate date) {
        this.status = status;
        this.error = message;
        this.date = date;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String message) {
        this.error = message;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
