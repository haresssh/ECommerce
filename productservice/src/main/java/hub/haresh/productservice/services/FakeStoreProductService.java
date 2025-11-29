package hub.haresh.productservice.services;

import hub.haresh.productservice.dtos.FakeStoreProductDto;
import hub.haresh.productservice.dtos.FakeStoreCreateProductDto;
import hub.haresh.productservice.exceptions.ProductNotFoundException;
import hub.haresh.productservice.models.Product;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service("fakeStoreProductService")
public class FakeStoreProductService implements ProductService {

    private RestTemplate restTemplate;
    private RedisTemplate<String, Object> redisTemplate;

    public FakeStoreProductService(RestTemplate restTemplate,
            RedisTemplate redisTemplate) {
        this.restTemplate = restTemplate;
        this.redisTemplate = redisTemplate;

    }

    @Override
    public Product getProductDetails(Long id) throws ProductNotFoundException {
        Product productFromCache = (Product) redisTemplate
                .opsForValue().get(String.valueOf(id));
        if (productFromCache != null) {
            return productFromCache;
        }
        // FakeStoreProductDto responseDto =
        // restTemplate.getForObject(
        // "https://fakestoreapi.com/products/" + id,
        // FakeStoreProductDto.class
        // );

        ResponseEntity<FakeStoreProductDto> responseEntity = restTemplate
                .getForEntity(
                        "https://fakestoreapi.com/products/" + id,
                        FakeStoreProductDto.class);

        if (responseEntity.getStatusCode() == HttpStatusCode.valueOf(404)) {
            // show some error to FE
        } else if (responseEntity.getStatusCode() == HttpStatusCode.valueOf(500)) {
            // tell FE that BE is not working currently
        }

        FakeStoreProductDto responseBody = responseEntity.getBody();
        if (responseBody == null) {
            throw new ProductNotFoundException("Product Not found");
        }

        Product product = responseBody.toProduct();

        if (true) {
            redisTemplate
                    .opsForValue()
                    .set(String.valueOf(id), product);
        }

        return product;
    }

    @Override
    public Product createProduct(String title, String description, String image, double price, String category) {
        FakeStoreCreateProductDto requestDto = new FakeStoreCreateProductDto();
        requestDto.setTitle(title);
        requestDto.setDescription(description);
        requestDto.setImage(image);
        requestDto.setPrice(price);
        requestDto.setCategory(category);

        FakeStoreProductDto responseDto = restTemplate.postForObject("https://fakestoreapi.com/products",
                requestDto,
                FakeStoreProductDto.class);

        return responseDto.toProduct();
    }

    @Override
    public List<Product> getAllProducts() {
        FakeStoreProductDto[] responseDto = restTemplate.getForObject(
                "https://fakestoreapi.com/products",
                FakeStoreProductDto[].class);

        List<Product> products = new ArrayList<>();
        for (FakeStoreProductDto dto : responseDto) {
            products.add(dto.toProduct());
        }

        return products;
    }
}
