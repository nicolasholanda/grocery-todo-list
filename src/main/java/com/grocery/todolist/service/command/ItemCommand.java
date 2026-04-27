package com.grocery.todolist.service.command;

import com.grocery.todolist.domain.GroceryItem;

public interface ItemCommand {

    void execute(GroceryItem item);
}
