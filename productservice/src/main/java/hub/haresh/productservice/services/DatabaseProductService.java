package hub.haresh.productservice.services;

import hub.haresh.productservice.exceptions.ProductNotFoundException;
import hub.haresh.productservice.models.Category;
import hub.haresh.productservice.models.Product;
import hub.haresh.productservice.repositories.CategoryRepository;
import hub.haresh.productservice.repositories.ProductRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("databaseProductService")
public class DatabaseProductService implements ProductService {

    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;

    public DatabaseProductService(ProductRepository productRepository,
            CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Cacheable(value = "products", key = "#id")
    public Product getProductDetails(Long id) throws ProductNotFoundException {
        Optional<Product> productOptionalFromDb = productRepository.findById(id);
        if (productOptionalFromDb.isEmpty()) {
            throw new ProductNotFoundException("Product not found with id: " + id);
        }
        return productOptionalFromDb.get();
    }

    @Override
    @CacheEvict(value = "products", allEntries = true)
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
