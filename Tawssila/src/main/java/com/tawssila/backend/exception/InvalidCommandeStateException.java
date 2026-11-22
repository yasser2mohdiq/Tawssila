package com.tawssila.backend.exception;

public class InvalidCommandeStateException extends RuntimeException {
    public InvalidCommandeStateException(String message) {
        super(message);
    }
}
