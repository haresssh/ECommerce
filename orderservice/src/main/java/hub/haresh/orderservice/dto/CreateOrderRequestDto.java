package hub.haresh.orderservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateOrderRequestDto {
    private Long userId;
    private Long productId;
    private Integer quantity;
}
