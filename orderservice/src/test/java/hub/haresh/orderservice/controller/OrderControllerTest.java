package hub.haresh.orderservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hub.haresh.orderservice.dto.CreateOrderRequestDto;
import hub.haresh.orderservice.dto.OrderResponseDto;
import hub.haresh.orderservice.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    private CreateOrderRequestDto createOrderRequestDto;
    private OrderResponseDto orderResponseDto;

    @BeforeEach
    void setUp() {
        createOrderRequestDto = new CreateOrderRequestDto();
        createOrderRequestDto.setUserId(1L);
        createOrderRequestDto.setProductId(1L);
        createOrderRequestDto.setQuantity(2);

        orderResponseDto = new OrderResponseDto();
        orderResponseDto.setId(1L);
        orderResponseDto.setStatus("PENDING");
    }

    @Test
    void createOrder_Success() throws Exception {
        when(orderService.createOrder(any(CreateOrderRequestDto.class))).thenReturn(orderResponseDto);

        mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createOrderRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }
}
