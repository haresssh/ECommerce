package hub.haresh.productservice.controllers;

import hub.haresh.productservice.commons.AuthenticationCommons;
import hub.haresh.productservice.dtos.CreateProductRequestDto;

import hub.haresh.productservice.exceptions.ProductNotFoundException;
import hub.haresh.productservice.models.Product;
import hub.haresh.productservice.services.ProductService;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController {

    private ProductService productService;

    public ProductController(@Qualifier("databaseProductService") ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        ResponseEntity<List<Product>> responseEntity = new ResponseEntity<>(products,
                HttpStatusCode.valueOf(200));

        return responseEntity;
    }

    @GetMapping("/products/{id}")
    public Product getProductDetails(@PathVariable("id") Long id) throws ProductNotFoundException {
        return productService.getProductDetails(id);
    }

    @PostMapping("/products")
    public ResponseEntity<Product> createProduct(@RequestBody CreateProductRequestDto requestDto) {
        Product product = productService.createProduct(
                requestDto.getTitle(),
                requestDto.getDescription(),
                requestDto.getImage(),
                requestDto.getPrice(),
                requestDto.getCategory());

        ResponseEntity<Product> responseEntity = new ResponseEntity<>(product, HttpStatusCode.valueOf(201));

        return responseEntity;
    }
}
