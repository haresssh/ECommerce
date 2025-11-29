package hub.haresh.productservice.repositories;

import hub.haresh.productservice.models.Product;
import hub.haresh.productservice.repositories.projections.ProductTitleAndDesc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Product save(Product product);

    Optional<Product> findById(Long id);

    List<Product> findAll();

    Optional<Product> findByTitleAndCategory_name(String title, String categoryName);

    @Query("select p from Product p where p.category.name = :categoryName")
    List<Product> getProductDate(@Param("categoryName") String categoryName);

    @Query(value = "select * from product where id = :id", nativeQuery = true)
    Product getProductData2(@Param("id") Long id);

    @Query(value = "select title, description from product where id = :id", nativeQuery = true)
    ProductTitleAndDesc getProductData3(@Param("id") Long id);

    Page<Product> findByTitleContaining(String query, Pageable pageable);
}
