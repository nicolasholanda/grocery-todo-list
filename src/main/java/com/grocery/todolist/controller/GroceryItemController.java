package com.grocery.todolist.controller;

import com.grocery.todolist.dto.request.CreateItemRequest;
import com.grocery.todolist.dto.response.ItemResponse;
import com.grocery.todolist.mapper.ItemMapper;
import com.grocery.todolist.service.GroceryItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class GroceryItemController {

    private final GroceryItemService service;
    private final ItemMapper mapper;

    @GetMapping
    public List<ItemResponse> listAll() {
        return mapper.toResponseList(service.listAll());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemResponse add(@Valid @RequestBody CreateItemRequest request) {
        return mapper.toResponse(service.add(request.getName(), request.getQuantity(), request.getUnit()));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(@PathVariable Long id) {
        service.remove(id);
    }

    @PatchMapping("/{id}/done")
    public ItemResponse markDone(@PathVariable Long id) {
        return mapper.toResponse(service.markDone(id));
    }

    @PatchMapping("/{id}/undo")
    public ItemResponse undo(@PathVariable Long id) {
        return mapper.toResponse(service.undo(id));
    }

    @PatchMapping("/{id}/redo")
    public ItemResponse redo(@PathVariable Long id) {
        return mapper.toResponse(service.redo(id));
    }
}
