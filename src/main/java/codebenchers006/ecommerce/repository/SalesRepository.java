package codebenchers006.ecommerce.repository;

import codebenchers006.ecommerce.model.Inventory;
import codebenchers006.ecommerce.model.Product;
import codebenchers006.ecommerce.model.Sales;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalesRepository extends JpaRepository<Sales,Integer> {

    public Sales findByProduct(Product prod);
}
