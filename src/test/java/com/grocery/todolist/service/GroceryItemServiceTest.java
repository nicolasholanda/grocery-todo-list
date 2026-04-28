package com.grocery.todolist.service;

import com.grocery.todolist.domain.GroceryItem;
import com.grocery.todolist.domain.ItemStatus;
import com.grocery.todolist.exception.InvalidItemStateException;
import com.grocery.todolist.exception.ItemNotFoundException;
import com.grocery.todolist.repository.GroceryItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GroceryItemServiceTest {

    @Mock
    private GroceryItemRepository repository;

    @InjectMocks
    private GroceryItemServiceImpl service;

    private GroceryItem pendingItem;
    private GroceryItem doneItem;

    @BeforeEach
    void setUp() {
        pendingItem = GroceryItem.builder()
                .id(1L)
                .name("Milk")
                .quantity(new BigDecimal("2.000"))
                .unit("L")
                .status(ItemStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        doneItem = GroceryItem.builder()
                .id(2L)
                .name("Eggs")
                .quantity(new BigDecimal("12.000"))
                .unit("units")
                .status(ItemStatus.DONE)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    void listAll_returnsAllItems() {
        when(repository.findAllByOrderByCreatedAtDesc()).thenReturn(List.of(doneItem, pendingItem));

        List<GroceryItem> result = service.listAll();

        assertThat(result).hasSize(2);
        verify(repository).findAllByOrderByCreatedAtDesc();
    }

    @Test
    void add_savesAndReturnsItem() {
        when(repository.save(any(GroceryItem.class))).thenReturn(pendingItem);

        GroceryItem result = service.add("Milk", new BigDecimal("2.000"), "L");

        assertThat(result.getName()).isEqualTo("Milk");
        verify(repository).save(any(GroceryItem.class));
    }

    @Test
    void remove_deletesExistingItem() {
        when(repository.findById(1L)).thenReturn(Optional.of(pendingItem));

        service.remove(1L);

        verify(repository).delete(pendingItem);
    }

    @Test
    void remove_throwsWhenItemNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.remove(99L))
                .isInstanceOf(ItemNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void markDone_transitionsPendingToDone() {
        when(repository.findById(1L)).thenReturn(Optional.of(pendingItem));
        when(repository.save(pendingItem)).thenReturn(pendingItem);

        GroceryItem result = service.markDone(1L);

        assertThat(result.getStatus()).isEqualTo(ItemStatus.DONE);
        verify(repository).save(pendingItem);
    }

    @Test
    void markDone_throwsWhenAlreadyDone() {
        when(repository.findById(2L)).thenReturn(Optional.of(doneItem));

        assertThatThrownBy(() -> service.markDone(2L))
                .isInstanceOf(InvalidItemStateException.class);
    }

    @Test
    void markDone_throwsWhenItemNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.markDone(99L))
                .isInstanceOf(ItemNotFoundException.class);
    }

    @Test
    void undo_transitionsDoneToPending() {
        when(repository.findById(2L)).thenReturn(Optional.of(doneItem));
        when(repository.save(doneItem)).thenReturn(doneItem);

        GroceryItem result = service.undo(2L);

        assertThat(result.getStatus()).isEqualTo(ItemStatus.PENDING);
        verify(repository).save(doneItem);
    }

    @Test
    void undo_throwsWhenAlreadyPending() {
        when(repository.findById(1L)).thenReturn(Optional.of(pendingItem));

        assertThatThrownBy(() -> service.undo(1L))
                .isInstanceOf(InvalidItemStateException.class);
    }

    @Test
    void undo_throwsWhenItemNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.undo(99L))
                .isInstanceOf(ItemNotFoundException.class);
    }

    @Test
    void redo_transitionsPendingToDone() {
        when(repository.findById(1L)).thenReturn(Optional.of(pendingItem));
        when(repository.save(pendingItem)).thenReturn(pendingItem);

        GroceryItem result = service.redo(1L);

        assertThat(result.getStatus()).isEqualTo(ItemStatus.DONE);
        verify(repository).save(pendingItem);
    }

    @Test
    void redo_throwsWhenAlreadyDone() {
        when(repository.findById(2L)).thenReturn(Optional.of(doneItem));

        assertThatThrownBy(() -> service.redo(2L))
                .isInstanceOf(InvalidItemStateException.class);
    }

    @Test
    void redo_throwsWhenItemNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.redo(99L))
                .isInstanceOf(ItemNotFoundException.class);
    }
}
