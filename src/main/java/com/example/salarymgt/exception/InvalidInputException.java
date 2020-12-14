package com.example.salarymgt.exception;

/**
 * Created by Jiang Wensi on 11/12/2020
 */
public class InvalidInputException extends RuntimeException {
    public InvalidInputException(String message) {
        super(message);
    }
}
