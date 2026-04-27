package com.grocery.todolist.exception;

public class ItemNotFoundException extends RuntimeException {

    public ItemNotFoundException(Long id) {
        super("Grocery item not found with id: " + id);
    }
}
