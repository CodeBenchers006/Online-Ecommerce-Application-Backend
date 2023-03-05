package codebenchers006.ecommerce.repository;

import codebenchers006.ecommerce.model.Category;
import codebenchers006.ecommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepo extends JpaRepository<Product,Integer> {


    Product findByName(String productName);

    List<Product> findByCategory(Category category);
}
