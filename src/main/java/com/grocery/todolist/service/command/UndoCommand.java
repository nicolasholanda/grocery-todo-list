package com.grocery.todolist.service.command;

import com.grocery.todolist.domain.GroceryItem;
import com.grocery.todolist.domain.ItemStatus;
import com.grocery.todolist.exception.InvalidItemStateException;

public class UndoCommand implements ItemCommand {

    @Override
    public void execute(GroceryItem item) {
        if (item.getStatus() != ItemStatus.DONE) {
            throw new InvalidItemStateException("Item must be DONE to be undone.");
        }
        item.setStatus(ItemStatus.PENDING);
    }
}
