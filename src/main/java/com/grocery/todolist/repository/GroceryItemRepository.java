package com.grocery.todolist.repository;

import com.grocery.todolist.domain.GroceryItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroceryItemRepository extends JpaRepository<GroceryItem, Long> {

    List<GroceryItem> findAllByOrderByCreatedAtDesc();
}
