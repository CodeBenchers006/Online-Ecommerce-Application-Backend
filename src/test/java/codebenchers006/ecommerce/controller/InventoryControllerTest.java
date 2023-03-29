package codebenchers006.ecommerce.controller;

import codebenchers006.ecommerce.model.Inventory;
import codebenchers006.ecommerce.model.Product;
import codebenchers006.ecommerce.service.InventoryService;
import codebenchers006.ecommerce.service.ProductService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class InventoryControllerTest {


    @InjectMocks
    InventoryController inventoryController;

    @Mock
    InventoryService inventoryService;

    @Mock
    ProductService productService;


    @Test
    public void testGetAllInventories() {
        // Mock the inventory service to return a non-empty inventory list
        List<Inventory> mockInventoryList = new ArrayList<>();
        mockInventoryList.add(new Inventory());
        mockInventoryList.add(new Inventory());
        Mockito.when(inventoryService.getInventoryList()).thenReturn(mockInventoryList);

        // Call the controller method
        ResponseEntity<?> response = inventoryController.getAllInventories();

        // Verify that the inventory service getInventoryList method was called
        Mockito.verify(inventoryService, Mockito.times(1)).getInventoryList();

        // Assert that the response is a success with the expected inventory list
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockInventoryList, response.getBody());
    }

    @Test
    public void testGetAllInventoriesEmptyList(){

        List<Inventory> inventoryList=new ArrayList<>();
        Mockito.when(inventoryService.getInventoryList()).thenReturn(inventoryList);

        ResponseEntity<?> response = inventoryController.getAllInventories();
        Mockito.verify(inventoryService, Mockito.times(1)).getInventoryList();
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

    }


    @Test
    public void testGetByProduct() {
        // Mock the product service to return a product with id 1
        Product mockProduct = new Product();
        Mockito.when(productService.findByProductId(1)).thenReturn(mockProduct);

        // Mock the inventory service to return an inventory item for the mock product
        Inventory mockInventory = new Inventory(1, mockProduct, 10);
        Mockito.when(inventoryService.findInventoryItemsByProduct(mockProduct)).thenReturn(mockInventory);

        // Call the controller method with product id 1
        ResponseEntity<?> response = inventoryController.getByProduct(1);

        // Verify that the product service findByProductId method was called with id 1
        Mockito.verify(productService, Mockito.times(1)).findByProductId(1);

        // Verify that the inventory service findInventoryItemsByProduct method was called with the mock product
        Mockito.verify(inventoryService, Mockito.times(1)).findInventoryItemsByProduct(mockProduct);

        // Assert that the response is a success with the expected inventory item
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockInventory, response.getBody());
    }
}
