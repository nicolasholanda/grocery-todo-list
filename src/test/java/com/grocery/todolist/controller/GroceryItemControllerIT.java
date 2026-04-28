package com.grocery.todolist.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grocery.todolist.domain.GroceryItem;
import com.grocery.todolist.domain.ItemStatus;
import com.grocery.todolist.repository.GroceryItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class GroceryItemControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private GroceryItemRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    private GroceryItem savedPending;
    private GroceryItem savedDone;

    @BeforeEach
    void setUp() {
        savedPending = repository.save(GroceryItem.builder()
                .name("Milk")
                .quantity(new BigDecimal("2.000"))
                .unit("L")
                .status(ItemStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build());

        savedDone = repository.save(GroceryItem.builder()
                .name("Eggs")
                .quantity(new BigDecimal("12.000"))
                .unit("units")
                .status(ItemStatus.DONE)
                .createdAt(LocalDateTime.now())
                .build());
    }

    @Test
    void listAll_returnsAllItems() throws Exception {
        mockMvc.perform(get("/api/items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))));
    }

    @Test
    void add_createsItemAndReturns201() throws Exception {
        Map<String, Object> body = Map.of(
                "name", "Bread",
                "quantity", "1.000",
                "unit", "kg"
        );

        mockMvc.perform(post("/api/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Bread"))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void add_returns400WhenBodyIsInvalid() throws Exception {
        Map<String, Object> body = Map.of(
                "name", "",
                "quantity", "-1",
                "unit", ""
        );

        mockMvc.perform(post("/api/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasSize(greaterThanOrEqualTo(1))));
    }

    @Test
    void remove_deletesItemAndReturns204() throws Exception {
        mockMvc.perform(delete("/api/items/{id}", savedPending.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void remove_returns404WhenItemNotFound() throws Exception {
        mockMvc.perform(delete("/api/items/{id}", 99999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void markDone_transitionsPendingToDone() throws Exception {
        mockMvc.perform(patch("/api/items/{id}/done", savedPending.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("DONE"));
    }

    @Test
    void markDone_returns400WhenAlreadyDone() throws Exception {
        mockMvc.perform(patch("/api/items/{id}/done", savedDone.getId()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void markDone_returns404WhenItemNotFound() throws Exception {
        mockMvc.perform(patch("/api/items/{id}/done", 99999L))
                .andExpect(status().isNotFound());
    }

    @Test
    void undo_transitionsDoneToPending() throws Exception {
        mockMvc.perform(patch("/api/items/{id}/undo", savedDone.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void undo_returns400WhenAlreadyPending() throws Exception {
        mockMvc.perform(patch("/api/items/{id}/undo", savedPending.getId()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void undo_returns404WhenItemNotFound() throws Exception {
        mockMvc.perform(patch("/api/items/{id}/undo", 99999L))
                .andExpect(status().isNotFound());
    }

    @Test
    void redo_transitionsPendingToDone() throws Exception {
        mockMvc.perform(patch("/api/items/{id}/redo", savedPending.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("DONE"));
    }

    @Test
    void redo_returns400WhenAlreadyDone() throws Exception {
        mockMvc.perform(patch("/api/items/{id}/redo", savedDone.getId()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void redo_returns404WhenItemNotFound() throws Exception {
        mockMvc.perform(patch("/api/items/{id}/redo", 99999L))
                .andExpect(status().isNotFound());
    }
}
