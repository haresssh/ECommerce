package hub.haresh.orderservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hub.haresh.orderservice.dto.CreateOrderRequestDto;
import hub.haresh.orderservice.dto.OrderResponseDto;
import hub.haresh.orderservice.exceptions.OrderNotFoundException;
import hub.haresh.orderservice.model.Order;
import hub.haresh.orderservice.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
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

    private Order order;
    private CreateOrderRequestDto createOrderRequestDto;

    @BeforeEach
    void setUp() {
        order = new Order();
        order.setId(1L);
        order.setUserId(1L);
        order.setProductId(1L);
        order.setQuantity(5);
        order.setStatus("PENDING");

        createOrderRequestDto = new CreateOrderRequestDto();
        createOrderRequestDto.setUserId(1L);
        createOrderRequestDto.setProductId(1L);
        createOrderRequestDto.setQuantity(5);
    }

    @Test
    void createOrder_Success() throws JsonProcessingException {
        when(restTemplate.getForObject(eq("http://localhost:8081/products/1"), eq(Object.class)))
                .thenReturn(new Object());
        when(restTemplate.getForObject(eq("http://localhost:8080/users/1"), eq(Object.class)))
                .thenReturn(new Object());
        when(restTemplate.postForEntity(eq("http://localhost:8084/inventory/reduce"), any(), eq(Object.class)))
                .thenReturn(org.springframework.http.ResponseEntity.ok().build());
        when(orderRepository.save(any(Order.class))).thenReturn(order);
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
                .thenThrow(new RestClientException("Product not found"));

        assertThrows(RestClientException.class, () -> orderService.createOrder(createOrderRequestDto));

        verify(orderRepository, never()).save(any(Order.class));
        verify(kafkaTemplate, never()).send(anyString(), anyString());
    }

    @Test
    void createOrder_UserNotFound() {
        when(restTemplate.getForObject(eq("http://localhost:8081/products/1"), eq(Object.class)))
                .thenReturn(new Object());
        when(restTemplate.getForObject(eq("http://localhost:8080/users/1"), eq(Object.class)))
                .thenThrow(new RestClientException("User not found"));

        assertThrows(RestClientException.class, () -> orderService.createOrder(createOrderRequestDto));

        verify(orderRepository, never()).save(any(Order.class));
        verify(kafkaTemplate, never()).send(anyString(), anyString());
    }

    @Test
    void getOrder_Success() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        OrderResponseDto response = orderService.getOrder(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
    }

    @Test
    void getOrder_NotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> orderService.getOrder(1L));
    }
}
