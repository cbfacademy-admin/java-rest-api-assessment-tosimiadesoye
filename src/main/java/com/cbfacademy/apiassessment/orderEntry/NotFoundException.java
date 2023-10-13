package com.cbfacademy.apiassessment.orderEntry;


public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}

class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
