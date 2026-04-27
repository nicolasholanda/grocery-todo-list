package com.grocery.todolist.service.command;

import com.grocery.todolist.domain.GroceryItem;
import com.grocery.todolist.domain.ItemStatus;
import com.grocery.todolist.exception.InvalidItemStateException;

public class MarkDoneCommand implements ItemCommand {

    @Override
    public void execute(GroceryItem item) {
        if (item.getStatus() != ItemStatus.PENDING) {
            throw new InvalidItemStateException("Item must be PENDING to be marked as done.");
        }
        item.setStatus(ItemStatus.DONE);
    }
}
