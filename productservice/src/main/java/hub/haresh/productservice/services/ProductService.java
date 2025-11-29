package hub.haresh.productservice.services;

import hub.haresh.productservice.exceptions.ProductNotFoundException;
import hub.haresh.productservice.models.Product;

import java.util.List;

public interface ProductService {
    public Product getProductDetails(Long id) throws ProductNotFoundException;

    public Product createProduct(String title, String description, String image, double price, String category);

    public List<Product> getAllProducts();
}
