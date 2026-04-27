package com.grocery.todolist.service;

import com.grocery.todolist.domain.GroceryItem;

import java.math.BigDecimal;
import java.util.List;

public interface GroceryItemService {

    List<GroceryItem> listAll();

    GroceryItem add(String name, BigDecimal quantity, String unit);

    void remove(Long id);

    GroceryItem markDone(Long id);

    GroceryItem undo(Long id);

    GroceryItem redo(Long id);
}
