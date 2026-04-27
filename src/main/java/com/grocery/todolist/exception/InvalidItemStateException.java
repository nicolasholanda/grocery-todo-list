package com.grocery.todolist.exception;

public class InvalidItemStateException extends RuntimeException {

    public InvalidItemStateException(String message) {
        super(message);
    }
}
