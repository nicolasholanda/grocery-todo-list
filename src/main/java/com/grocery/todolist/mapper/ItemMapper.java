package com.grocery.todolist.mapper;

import com.grocery.todolist.domain.GroceryItem;
import com.grocery.todolist.dto.response.ItemResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ItemMapper {

    public ItemResponse toResponse(GroceryItem item) {
        return ItemResponse.builder()
                .id(item.getId())
                .name(item.getName())
                .quantity(item.getQuantity())
                .unit(item.getUnit())
                .status(item.getStatus())
                .createdAt(item.getCreatedAt())
                .build();
    }

    public List<ItemResponse> toResponseList(List<GroceryItem> items) {
        return items.stream().map(this::toResponse).toList();
    }
}
