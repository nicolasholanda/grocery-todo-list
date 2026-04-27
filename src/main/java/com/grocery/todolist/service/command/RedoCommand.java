package com.grocery.todolist.service.command;

import com.grocery.todolist.domain.GroceryItem;
import com.grocery.todolist.domain.ItemStatus;
import com.grocery.todolist.exception.InvalidItemStateException;

public class RedoCommand implements ItemCommand {

    @Override
    public void execute(GroceryItem item) {
        if (item.getStatus() != ItemStatus.PENDING) {
            throw new InvalidItemStateException("Item must be PENDING to be redone.");
        }
        item.setStatus(ItemStatus.DONE);
    }
}
