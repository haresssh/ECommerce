package hub.haresh.orderservice.service;

import hub.haresh.orderservice.dto.CreateOrderRequestDto;
import hub.haresh.orderservice.dto.OrderResponseDto;
import hub.haresh.orderservice.model.Order;
import hub.haresh.orderservice.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final org.springframework.web.client.RestTemplate restTemplate;
    private final org.springframework.kafka.core.KafkaTemplate<String, String> kafkaTemplate;
    private final com.fasterxml.jackson.databind.ObjectMapper objectMapper;

    public OrderService(OrderRepository orderRepository,
            org.springframework.web.client.RestTemplate restTemplate,
            org.springframework.kafka.core.KafkaTemplate<String, String> kafkaTemplate,
            com.fasterxml.jackson.databind.ObjectMapper objectMapper) {
        this.orderRepository = orderRepository;
        this.restTemplate = restTemplate;
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public OrderResponseDto createOrder(CreateOrderRequestDto requestDto) {
        // 1. Validate Product
        try {
            restTemplate.getForObject("http://localhost:8081/products/" + requestDto.getProductId(), Object.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Product not found or service unavailable: " + e.getMessage());
        }

        // 2. Validate User
        try {
            restTemplate.getForObject("http://localhost:8080/users/" + requestDto.getUserId(), Object.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("User not found or service unavailable: " + e.getMessage());
        }

        Order order = new Order();
        order.setUserId(requestDto.getUserId());
        order.setProductId(requestDto.getProductId());
        order.setQuantity(requestDto.getQuantity());
        order.setStatus("PENDING");

        Order savedOrder = orderRepository.save(order);

        // 3. Send Email
        hub.haresh.orderservice.dto.SendEmailDto emailDto = new hub.haresh.orderservice.dto.SendEmailDto();
        emailDto.setFromEmail("orders@ecommerce.com");
        emailDto.setToEmail("user" + requestDto.getUserId() + "@example.com"); // Simplified email
        emailDto.setSubject("Order Confirmed");
        emailDto.setBody("Your order " + savedOrder.getId() + " has been placed.");

        try {
            String message = objectMapper.writeValueAsString(emailDto);
            kafkaTemplate.send("emailSend", message);
        } catch (Exception e) {
            // Log error but don't fail order creation
            e.printStackTrace();
        }

        return OrderResponseDto.fromOrder(savedOrder);
    }

    public OrderResponseDto getOrder(Long id) {
        Optional<Order> orderOptional = orderRepository.findById(id);
        if (orderOptional.isPresent()) {
            return OrderResponseDto.fromOrder(orderOptional.get());
        } else {
            throw new RuntimeException("Order not found with id: " + id);
        }
    }
}
