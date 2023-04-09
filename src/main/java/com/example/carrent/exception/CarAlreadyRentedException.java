package com.example.carrent.exception;

public class CarAlreadyRentedException extends RuntimeException {
    public CarAlreadyRentedException(String message) {
        super(message);
    }
}
