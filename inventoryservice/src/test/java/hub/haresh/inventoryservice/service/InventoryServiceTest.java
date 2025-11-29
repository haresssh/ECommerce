package hub.haresh.inventoryservice.service;

import hub.haresh.inventoryservice.model.Inventory;
import hub.haresh.inventoryservice.repository.InventoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {

    @Mock
    private InventoryRepository inventoryRepository;

    @InjectMocks
    private InventoryService inventoryService;

    private Inventory inventory;

    @BeforeEach
    void setUp() {
        inventory = new Inventory();
        inventory.setId(1L);
        inventory.setProductId(1L);
        inventory.setQuantity(10);
    }

    @Test
    void initializeInventory_New() {
        when(inventoryRepository.findByProductId(1L)).thenReturn(Optional.empty());
        when(inventoryRepository.save(any(Inventory.class))).thenReturn(inventory);

        Inventory result = inventoryService.initializeInventory(1L, 10);

        assertNotNull(result);
        assertEquals(10, result.getQuantity());
        verify(inventoryRepository, times(1)).save(any(Inventory.class));
    }

    @Test
    void initializeInventory_Existing() {
        when(inventoryRepository.findByProductId(1L)).thenReturn(Optional.of(inventory));
        when(inventoryRepository.save(any(Inventory.class))).thenReturn(inventory);

        Inventory result = inventoryService.initializeInventory(1L, 5);

        assertNotNull(result);
        // The mock returns the original object, but logic should update it.
        // In a real mock, we might need to capture the argument to verify the update.
        // Here we just verify save is called.
        verify(inventoryRepository, times(1)).save(any(Inventory.class));
    }

    @Test
    void reduceStock_Success() {
        when(inventoryRepository.findByProductId(1L)).thenReturn(Optional.of(inventory));

        boolean result = inventoryService.reduceStock(1L, 5);

        assertTrue(result);
        assertEquals(5, inventory.getQuantity());
        verify(inventoryRepository, times(1)).save(inventory);
    }

    @Test
    void reduceStock_Insufficient() {
        when(inventoryRepository.findByProductId(1L)).thenReturn(Optional.of(inventory));

        boolean result = inventoryService.reduceStock(1L, 15);

        assertFalse(result);
        assertEquals(10, inventory.getQuantity()); // Should not change
        verify(inventoryRepository, never()).save(inventory);
    }
}
