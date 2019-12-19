package edu.lnu.musicly.streaming.exceptions;

public class ValidationException extends RuntimeException {
    public ValidationException(String msg, Object... args) {
        super(String.format(msg, args));
    }
}
