package hub.haresh.inventoryservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hub.haresh.inventoryservice.model.Inventory;
import hub.haresh.inventoryservice.service.InventoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(InventoryController.class)
class InventoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InventoryService inventoryService;

    @Autowired
    private ObjectMapper objectMapper;

    private Inventory inventory;

    @BeforeEach
    void setUp() {
        inventory = new Inventory();
        inventory.setId(1L);
        inventory.setProductId(1L);
        inventory.setQuantity(10);
    }

    @Test
    void getInventory_Success() throws Exception {
        when(inventoryService.getInventory(1L)).thenReturn(inventory);

        mockMvc.perform(get("/inventory/1"))
                .andExpect(status().isOk());
    }

    @Test
    void reduceStock_Success() throws Exception {
        when(inventoryService.reduceStock(anyLong(), anyInt())).thenReturn(true);

        Map<String, Object> payload = new HashMap<>();
        payload.put("productId", 1);
        payload.put("quantity", 5);

        mockMvc.perform(post("/inventory/reduce")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isOk());
    }

    @Test
    void reduceStock_Insufficient() throws Exception {
        when(inventoryService.reduceStock(anyLong(), anyInt())).thenReturn(false);

        Map<String, Object> payload = new HashMap<>();
        payload.put("productId", 1);
        payload.put("quantity", 15);

        mockMvc.perform(post("/inventory/reduce")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isBadRequest());
    }
}
