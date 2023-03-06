package codebenchers006.ecommerce.repository;

import codebenchers006.ecommerce.model.Inventory;
import codebenchers006.ecommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<Inventory,Integer> {


    public Inventory findByProduct(Product prod);
}
