package com.grocery.todolist.service;

import com.grocery.todolist.domain.GroceryItem;
import com.grocery.todolist.exception.ItemNotFoundException;
import com.grocery.todolist.repository.GroceryItemRepository;
import com.grocery.todolist.service.command.MarkDoneCommand;
import com.grocery.todolist.service.command.RedoCommand;
import com.grocery.todolist.service.command.UndoCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GroceryItemServiceImpl implements GroceryItemService {

    private final GroceryItemRepository repository;

    @Override
    public List<GroceryItem> listAll() {
        return repository.findAllByOrderByCreatedAtDesc();
    }

    @Override
    @Transactional
    public GroceryItem add(String name, BigDecimal quantity, String unit) {
        GroceryItem item = GroceryItem.builder()
                .name(name)
                .quantity(quantity)
                .unit(unit)
                .build();
        return repository.save(item);
    }

    @Override
    @Transactional
    public void remove(Long id) {
        GroceryItem item = findOrThrow(id);
        repository.delete(item);
    }

    @Override
    @Transactional
    public GroceryItem markDone(Long id) {
        GroceryItem item = findOrThrow(id);
        new MarkDoneCommand().execute(item);
        return repository.save(item);
    }

    @Override
    @Transactional
    public GroceryItem undo(Long id) {
        GroceryItem item = findOrThrow(id);
        new UndoCommand().execute(item);
        return repository.save(item);
    }

    @Override
    @Transactional
    public GroceryItem redo(Long id) {
        GroceryItem item = findOrThrow(id);
        new RedoCommand().execute(item);
        return repository.save(item);
    }

    private GroceryItem findOrThrow(Long id) {
        return repository.findById(id).orElseThrow(() -> new ItemNotFoundException(id));
    }
}
