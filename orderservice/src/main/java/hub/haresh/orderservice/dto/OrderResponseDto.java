package hub.haresh.orderservice.dto;

import hub.haresh.orderservice.model.Order;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class OrderResponseDto {
    private Long id;
    private Long userId;
    private Long productId;
    private Integer quantity;
    private String status;
    private LocalDateTime createdAt;

    public static OrderResponseDto fromOrder(Order order) {
        OrderResponseDto dto = new OrderResponseDto();
        dto.setId(order.getId());
        dto.setUserId(order.getUserId());
        dto.setProductId(order.getProductId());
        dto.setQuantity(order.getQuantity());
        dto.setStatus(order.getStatus());
        dto.setCreatedAt(order.getCreatedAt());
        return dto;
    }
}
