package com.example.carrent.exception;

import org.springframework.http.HttpStatus;

public class InValidDateException extends RuntimeException {
    public InValidDateException(String message) {
        super(message);
    }
}
