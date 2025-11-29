package hub.haresh.orderservice.controller;

import hub.haresh.orderservice.dto.CreateOrderRequestDto;
import hub.haresh.orderservice.dto.OrderResponseDto;
import hub.haresh.orderservice.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(@RequestBody CreateOrderRequestDto requestDto) {
        OrderResponseDto responseDto = orderService.createOrder(requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDto> getOrder(@PathVariable Long id) {
        try {
            OrderResponseDto responseDto = orderService.getOrder(id);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
