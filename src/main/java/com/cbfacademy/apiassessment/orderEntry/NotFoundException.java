package com.cbfacademy.apiassessment.orderEntry;


public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}

class ValidationException extends Exception {
    public ValidationException(String message) {
        super(message);
    }
}

class FileNameException extends Exception{
    public FileNameException(String message) {super((message));}
}