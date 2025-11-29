package hub.haresh.productservice.services;

import hub.haresh.productservice.models.Product;
import hub.haresh.productservice.repositories.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class SearchService {
    private ProductRepository productRepository;

    public SearchService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Page<Product> search(String query, int pageNumber, int size) {
        Sort sort = Sort.by("title").descending();

        Pageable pageable = PageRequest.of(pageNumber, size, sort);

        return productRepository.findByTitleContaining(query, pageable);
    }
}
