package com.grocery.todolist.dto.response;

import com.grocery.todolist.domain.ItemStatus;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class ItemResponse {

    private Long id;
    private String name;
    private BigDecimal quantity;
    private String unit;
    private ItemStatus status;
    private LocalDateTime createdAt;
}
