package hub.haresh.productservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FakeStoreCreateProductDto {
    String title;
    String description;
    String image;
    double price;
    String category;
}
