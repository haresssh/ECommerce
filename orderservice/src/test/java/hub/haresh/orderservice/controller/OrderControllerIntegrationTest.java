package hub.haresh.orderservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hub.haresh.orderservice.dto.CreateOrderRequestDto;
import hub.haresh.orderservice.model.Order;
import hub.haresh.orderservice.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrderRepository orderRepository;

    @MockBean
    private RestTemplate restTemplate;

    @MockBean
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        orderRepository.deleteAll();
    }

    @Test
    void createOrder_IntegrationFlow() throws Exception {
        // Mock external services
        when(restTemplate.getForObject(eq("http://localhost:8081/products/1"), eq(Object.class)))
                .thenReturn(new Object());
        when(restTemplate.getForObject(eq("http://localhost:8080/users/1"), eq(Object.class)))
                .thenReturn(new Object());

        CreateOrderRequestDto requestDto = new CreateOrderRequestDto();
        requestDto.setUserId(1L);
        requestDto.setProductId(1L);
        requestDto.setQuantity(5);

        mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("PENDING"));

        assertEquals(1, orderRepository.count());
        Order savedOrder = orderRepository.findAll().get(0);
        assertEquals(1L, savedOrder.getUserId());
        assertEquals(1L, savedOrder.getProductId());
        assertEquals(5, savedOrder.getQuantity());
    }
}
