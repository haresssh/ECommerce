package hub.haresh.productservice.services;

import hub.haresh.productservice.exceptions.ProductNotFoundException;
import hub.haresh.productservice.models.Category;
import hub.haresh.productservice.models.Product;
import hub.haresh.productservice.repositories.CategoryRepository;
import hub.haresh.productservice.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private DatabaseProductService productService;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(1L);
        product.setTitle("Test Product");
        product.setPrice(100.0);
        product.setDescription("Test Description");
        product.setImageUrl("http://image.com");
        Category category = new Category();
        category.setName("electronics");
        product.setCategory(category);
    }

    @Test
    void getProductDetails_Success() throws ProductNotFoundException {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Product result = productService.getProductDetails(1L);

        assertNotNull(result);
        assertEquals("Test Product", result.getTitle());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void getProductDetails_NotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.getProductDetails(1L));
    }

    @Test
    void createProduct_Success() {
        when(categoryRepository.findByName("electronics")).thenReturn(null);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product result = productService.createProduct("Test Product", "Test Description", "http://image.com", 100.0,
                "electronics");

        assertNotNull(result);
        assertEquals("Test Product", result.getTitle());
        verify(categoryRepository, times(1)).findByName("electronics");
        verify(productRepository, times(1)).save(any(Product.class));
    }
}
