package hub.haresh.productservice.services;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import hub.haresh.productservice.models.Category;
import hub.haresh.productservice.models.Product;
import hub.haresh.productservice.repositories.CategoryRepository;
import hub.haresh.productservice.repositories.ProductRepository;
import hub.haresh.productservice.exceptions.ProductNotFoundException;

@Service("databaseProductService")
public class DatabaseProductService implements ProductService {

    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;
    private RedisTemplate<String, Object> redisTemplate;

    public DatabaseProductService(ProductRepository productRepository,
            CategoryRepository categoryRepository,
            RedisTemplate redisTemplate) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Product getProductDetails(Long id) throws ProductNotFoundException {
        Product productFromCache = (Product) redisTemplate.opsForValue().get(String.valueOf(id));
        if (productFromCache != null) {
            return productFromCache;
        }

        // TODO: Add null check and throw ProductNotFound exception if product is not
        // found
        Optional<Product> productOptionalFromDb = productRepository.findById(id);
        if (productOptionalFromDb.isEmpty()) {
            // Throw an exception product not found
            System.out.println("Product not found");
            return null;
        }

        Product productFromDb = productOptionalFromDb.get();
        System.out.println(productFromDb.getTitle());

        redisTemplate.opsForValue().set(String.valueOf(id), productFromDb);

        return productFromDb;
    }

    @Override
    public Product createProduct(String title, String description, String image, double price, String categoryName) {
        Product product = new Product();
        product.setTitle(title);
        product.setDescription(description);
        product.setImageUrl(image);
        product.setPrice(price);

        Category categoryFromDatabase = categoryRepository.findByName(categoryName);
        if (categoryFromDatabase == null) {
            Category newCategory = new Category();
            newCategory.setName(categoryName);

            categoryFromDatabase = newCategory;

            // categoryFromDatabase = categoryRepository.save(category);
        }

        product.setCategory(categoryFromDatabase);

        return productRepository.save(product);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
}
