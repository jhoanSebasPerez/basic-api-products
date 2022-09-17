package com.example.basicapiproducts.dtos;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class ResponseDto {

    private int status;
    private Map data;
    private LocalDate date;

    public ResponseDto(int status, Map data, LocalDate date) {
        this.status = status;
        this.data = data;
        this.date = date;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Map getData() {
        return data;
    }

    public void setData(Map data) {
        this.data = data;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
