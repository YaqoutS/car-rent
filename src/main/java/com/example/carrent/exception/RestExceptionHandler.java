package com.example.carrent.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


//@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = EntityNotFoundException.class)
    public ResponseEntity<Object> handleApiRequestException(EntityNotFoundException exception) {
        ApiException apiException = new ApiException(HttpStatus.NOT_FOUND);
        apiException.setMessage(exception.getMessage());
        return buildResponseEntity(apiException);
    }

    @ExceptionHandler(value = InValidDateException.class)
    public ResponseEntity<Object> handleInValidDateException(InValidDateException exception) {
        ApiException apiException = new ApiException(HttpStatus.BAD_REQUEST);
        apiException.setMessage(exception.getMessage());
        return buildResponseEntity(apiException);
    }

    @ExceptionHandler(value = RequiredFieldException.class)
    public ResponseEntity<Object> handleRequiredFieldException(RequiredFieldException exception) {
        ApiException apiException = new ApiException(HttpStatus.BAD_REQUEST);
        apiException.setMessage(exception.getMessage());
        return buildResponseEntity(apiException);
    }

    private ResponseEntity<Object> buildResponseEntity(ApiException apiException) {
        return new ResponseEntity<>(apiException, apiException.getStatus());
    }



    //other exception handlers below

}
