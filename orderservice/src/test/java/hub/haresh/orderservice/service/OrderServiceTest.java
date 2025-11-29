package hub.haresh.orderservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hub.haresh.orderservice.dto.CreateOrderRequestDto;
import hub.haresh.orderservice.dto.OrderResponseDto;
import hub.haresh.orderservice.model.Order;
import hub.haresh.orderservice.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private OrderService orderService;

    private CreateOrderRequestDto createOrderRequestDto;
    private Order order;

    @BeforeEach
    void setUp() {
        createOrderRequestDto = new CreateOrderRequestDto();
        createOrderRequestDto.setUserId(1L);
        createOrderRequestDto.setProductId(1L);
        createOrderRequestDto.setQuantity(2);

        order = new Order();
        order.setId(1L);
        order.setUserId(1L);
        order.setProductId(1L);
        order.setQuantity(2);
        order.setStatus("PENDING");
    }

    @Test
    void createOrder_Success() throws JsonProcessingException {
        // Mock Product Service call
        when(restTemplate.getForObject(eq("http://localhost:8081/products/1"), eq(Object.class)))
                .thenReturn(new Object());

        // Mock User Service call
        when(restTemplate.getForObject(eq("http://localhost:8080/users/1"), eq(Object.class)))
                .thenReturn(new Object());

        // Mock Repository save
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        // Mock ObjectMapper
        when(objectMapper.writeValueAsString(any())).thenReturn("json-string");

        OrderResponseDto response = orderService.createOrder(createOrderRequestDto);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("PENDING", response.getStatus());

        verify(orderRepository, times(1)).save(any(Order.class));
        verify(kafkaTemplate, times(1)).send(eq("emailSend"), anyString());
    }

    @Test
    void createOrder_ProductNotFound() {
        when(restTemplate.getForObject(eq("http://localhost:8081/products/1"), eq(Object.class)))
                .thenThrow(new RuntimeException("Product not found"));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            orderService.createOrder(createOrderRequestDto);
        });

        assertTrue(exception.getMessage().contains("Product not found"));
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void createOrder_UserNotFound() {
        when(restTemplate.getForObject(eq("http://localhost:8081/products/1"), eq(Object.class)))
                .thenReturn(new Object());
        when(restTemplate.getForObject(eq("http://localhost:8080/users/1"), eq(Object.class)))
                .thenThrow(new RuntimeException("User not found"));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            orderService.createOrder(createOrderRequestDto);
        });

        assertTrue(exception.getMessage().contains("User not found"));
        verify(orderRepository, never()).save(any(Order.class));
    }
}
