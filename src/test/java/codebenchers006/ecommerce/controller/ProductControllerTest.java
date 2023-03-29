package codebenchers006.ecommerce.controller;

import codebenchers006.ecommerce.dto.ProductDTO;
import codebenchers006.ecommerce.exception.CustomException;
import codebenchers006.ecommerce.model.Category;
import codebenchers006.ecommerce.model.Product;
import codebenchers006.ecommerce.repository.CategoryRepo;
import codebenchers006.ecommerce.response.ApiResponse;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ProductControllerTest {

    @InjectMocks
    ProductController productController;

    @Mock
    ProductService productService;

    @Mock
    CategoryRepo categoryRepo;

    @Test
    public void testCreateProduct() {
        // create a ProductDTO object
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("Test Product");
        productDTO.setDescription("This is a test product.");
        productDTO.setPrice(9.99);
        productDTO.setCategoryId(1);

        // create a Category object
        Category category = new Category();
        category.setId(1);
        category.setCategoryName("Test Category");

        // mock the categoryRepo.findById() method to return the Category object
        when(categoryRepo.findById(1)).thenReturn(Optional.of(category));


        ResponseEntity<ApiResponse> response = productController.createProduct(productDTO);

        // verify that the productService.createProduct() method was called once
        verify(productService, times(1)).createProduct(productDTO, category);

        // verify the response entity
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Product has been created", response.getBody().getMessage());
    }

    @Test
    public void testShowProducts() {
        List<Product> productList = new ArrayList<>();
        productList.add(new Product());
        productList.add(new Product());
        productList.add(new Product());
        Mockito.when(productService.showProducts()).thenReturn(productList);

        List<Product> result = productController.showProducts();

        assertEquals(3, result.size());

    }

    @Test
    public void testGetProducts() {
        // Create a list of ProductDTO objects to return from the mock service
        List<ProductDTO> products = new ArrayList<>();
        products.add(new ProductDTO());
        products.add(new ProductDTO());

        Mockito.when(productService.getProducts()).thenReturn(products);

        // Call the controller method
        List<ProductDTO> result = productController.getProducts();

        // Verify that the mock service was called and returned the expected result
        Mockito.verify(productService).getProducts();
        assertEquals(products, result);
    }

    @Test
    public void testUpdateProduct() {
        // Create a mock ProductDTO object
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("Updated Product");
        productDTO.setPrice(20.0);
        productDTO.setCategoryId(1);

        // Create a mock ResponseEntity object
        ApiResponse apiResponse = new ApiResponse(true, "Product details updated successfully");
        ResponseEntity<ApiResponse> expectedResponse = new ResponseEntity<>(apiResponse, HttpStatus.OK);

        // Set up the mock ProductService object
        Mockito.when(productService.findByIdExist(1)).thenReturn(true);

        // Call the controller method with the mock objects
        ResponseEntity<ApiResponse> actualResponse = productController.updateProduct(1, productDTO);

        // Verify that the ProductService method was called with the correct arguments
        Mockito.verify(productService).updateProduct(1, productDTO);

        // Verify that the actual response matches the expected response
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void testDeleteProduct() {
        int productId = 1;
        Mockito.when(productService.findByIdExist(productId)).thenReturn(true);
        ResponseEntity<ApiResponse> responseEntity = productController.deleteProduct(productId);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Product deleted successfully", responseEntity.getBody().getMessage());
        Mockito.verify(productService, Mockito.times(1)).deleteProduct(productId);
    }

    @Test
    public void testDeleteProduct_NotFound() {
        int productId = 1;
        Mockito.when(productService.findByIdExist(productId)).thenReturn(false);
        ResponseEntity<ApiResponse> responseEntity = productController.deleteProduct(productId);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Product Id not found", responseEntity.getBody().getMessage());
        Mockito.verify(productService, Mockito.times(0)).deleteProduct(productId);
    }

    @Test
    public void testGetById() {
        int productId = 1;
        ProductDTO product = new ProductDTO();
        Mockito.when(productService.findByIdExist(productId)).thenReturn(true);
        Mockito.when(productService.getUsingId(productId)).thenReturn(product);
        ResponseEntity<ProductDTO> responseEntity = productController.getById(productId);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(product, responseEntity.getBody());
    }

    @Test
    public void testGetByIdNotFound() {
        int productId = 1;
        Mockito.when(productService.findByIdExist(productId)).thenReturn(false);
        assertThrows(CustomException.class,()->productController.getById(productId));
    }


    @Test
    void testGetByProductName() {
        // Arrange
        String productName = "TestProduct";
        Product product = new Product();
        Mockito.when(productService.findByProductName(productName)).thenReturn(product);

        // Act
        ResponseEntity<Product> responseEntity = productController.getByProductName(productName);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(product, responseEntity.getBody());
    }

    @Test
    public void testGetByCategoryId() {
        List<Product> productList = new ArrayList<>();
        productList.add(new Product());
        productList.add(new Product());

        Mockito.when(productService.findByCategoryId(1)).thenReturn(productList);

        ResponseEntity<List<Product>> responseEntity = productController.getByCategoryId(1);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(productList, responseEntity.getBody());
    }

}
