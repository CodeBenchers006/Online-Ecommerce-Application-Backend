package codebenchers006.ecommerce.service;

import codebenchers006.ecommerce.model.Inventory;
import codebenchers006.ecommerce.model.Product;
import codebenchers006.ecommerce.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryService {

    @Autowired
    InventoryRepository inventoryRepository;


    public List<Inventory> getInventoryList() {

        return inventoryRepository.findAll();
    }

    public void addInventory(Inventory inventory){

        inventoryRepository.save(inventory);
    }

    public Inventory findInventoryItemsByProduct(Product product){

        return inventoryRepository.findByProduct(product);
    }
}
