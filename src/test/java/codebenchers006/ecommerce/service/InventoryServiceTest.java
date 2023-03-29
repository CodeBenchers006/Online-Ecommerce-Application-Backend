package codebenchers006.ecommerce.service;

import codebenchers006.ecommerce.model.Inventory;
import codebenchers006.ecommerce.model.Product;
import codebenchers006.ecommerce.repository.InventoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class InventoryServiceTest {


    @InjectMocks
    InventoryService inventoryService;

    @Mock
    InventoryRepository inventoryRepository;

    @Test
    void getInventoryListTest(){

        Inventory inventory = new Inventory(1, new Product(),10);
        List<Inventory> inventoryList = new ArrayList<>();
        inventoryList.add(inventory);

        when(inventoryRepository.findAll()).thenReturn(inventoryList);
        Assertions.assertEquals(inventoryList.size(),inventoryService.getInventoryList().size());

    }

    @Test
    void addInventory(){
        Inventory inventory = new Inventory(1, new Product(),10);

        inventoryService.addInventory(inventory);

        verify(inventoryRepository).save(inventory);
    }

    @Test
    void findInventoryItemsByProduct(){

        Product p = new Product();
        Inventory inventory = new Inventory(1,p,10);

        when(inventoryRepository.findByProduct(p)).thenReturn(inventory);
        Assertions.assertEquals(inventory,inventoryService.findInventoryItemsByProduct(p));
    }

}
