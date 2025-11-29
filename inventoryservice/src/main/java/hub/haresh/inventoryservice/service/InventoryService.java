package hub.haresh.inventoryservice.service;

import hub.haresh.inventoryservice.model.Inventory;
import hub.haresh.inventoryservice.repository.InventoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public Inventory initializeInventory(Long productId, Integer quantity) {
        Optional<Inventory> existingInventory = inventoryRepository.findByProductId(productId);
        if (existingInventory.isPresent()) {
            Inventory inventory = existingInventory.get();
            inventory.setQuantity(inventory.getQuantity() + quantity);
            return inventoryRepository.save(inventory);
        } else {
            Inventory inventory = new Inventory();
            inventory.setProductId(productId);
            inventory.setQuantity(quantity);
            return inventoryRepository.save(inventory);
        }
    }

    public Inventory getInventory(Long productId) {
        return inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Inventory not found for product id: " + productId));
    }

    @Transactional
    public boolean reduceStock(Long productId, Integer quantity) {
        Inventory inventory = getInventory(productId);
        if (inventory.getQuantity() < quantity) {
            return false;
        }
        inventory.setQuantity(inventory.getQuantity() - quantity);
        inventoryRepository.save(inventory);
        return true;
    }
}
