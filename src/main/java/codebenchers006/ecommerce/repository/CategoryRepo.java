package codebenchers006.ecommerce.repository;

import codebenchers006.ecommerce.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepo extends JpaRepository<Category,Integer> {
    Category findByCategoryName(String categoryName);
}
