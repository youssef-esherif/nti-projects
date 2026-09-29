package org.example.exception;

public class InvalidOrderStateException extends RuntimeException {
    public InvalidOrderStateException(String msg) { super(msg); }
}