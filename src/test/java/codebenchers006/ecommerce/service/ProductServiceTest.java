package codebenchers006.ecommerce.service;

import codebenchers006.ecommerce.dto.ProductDTO;
import codebenchers006.ecommerce.model.Category;
import codebenchers006.ecommerce.model.Inventory;
import codebenchers006.ecommerce.model.Product;
import codebenchers006.ecommerce.repository.ProductRepo;
import codebenchers006.ecommerce.response.ApiResponse;
import codebenchers006.ecommerce.service.CategoryService;
import codebenchers006.ecommerce.service.InventoryService;
import codebenchers006.ecommerce.service.ProductService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@SpringBootTest
public class ProductServiceTest {

    @InjectMocks
    ProductService productService;

    @Mock
    ProductRepo productRepo;

    @Mock
    InventoryService inventoryService;

    @Mock
    CategoryService categoryService;

    @Test
    public void createProductTest() {

        Category category = new Category(1, null, null, null);
        ProductDTO productDTO = new ProductDTO(1, "abc", "url", 123, "des", category.getId(), 10);

        Product p = new Product(1,"abc","url",123,"des",category);

        Product expected = productService.createProduct(productDTO,category);

        //System.out.println(expected);
        Assertions.assertEquals(expected.getName(),p.getName());
    }

    @Test
    public void showProductsTest(){

        Category category = new Category(1, null, null, null);

        Product p = new Product(1,"abc","url",123,"des",category);
        List<Product> productList = new ArrayList<>();
        productList.add(p);

        when(productRepo.findAll()).thenReturn(productList);
        Assertions.assertEquals(1,productService.showProducts().size());
    }

    @Test
    public void getProductDtoTest(){

        Category c = new Category(1,"cname","cdes",null);
        Product p = new Product(1,"pname","purl",123,"pdes",c);
        ProductDTO productDTO = new ProductDTO(1,p.getName(),p.getImageUrl(),p.getPrice(),p.getDescription(),c.getId(),10);

        ProductDTO productDTO1= productService.getProductDto(p);
        Assertions.assertEquals(productDTO.getName(),productDTO1.getName());
    }

    @Test
    public void findBYIdTest(){

        Product p = new Product(1,"pname","purl",123,"pdes",new Category());

        when(productRepo.findById(1)).thenReturn(Optional.of(p));
        Assertions.assertEquals(true,productService.findByIdExist(1));
        Assertions.assertEquals(p.getProduct_id(),productService.findByProductId(p.getProduct_id()).getProduct_id());
    }

    @Test
    public void getUsingIdTest(){

        Category c = new Category(1,"cname","cdes",null);
        Product p = new Product(1,"pname","purl",123,"pdes",c);
        ProductDTO actual = new ProductDTO(1,p.getName(),p.getImageUrl(),p.getPrice(),p.getDescription(),c.getId(),10);

        when(productRepo.findById(1)).thenReturn(Optional.of(p));
        ProductDTO expected = productService.getUsingId(1);

        Assertions.assertEquals(expected.getName(),actual.getName());
    }

    @Test
    public void findByProductNameTest(){
        Category c = new Category(1,"cname","cdes",null);
        Product p = new Product(1,"pname","purl",123,"pdes",c);

        when(productRepo.findByName("cname")).thenReturn(p);
        Product ex = productService.findByProductName("cname");
        Assertions.assertEquals(ex.getProduct_id(),p.getProduct_id());
    }

    @Test
    public void findByCategoryIdTest(){

        Category c = new Category(1,"cname","cdes",null);
        Product p = new Product(1,"pname","purl",123,"pdes",c);

        List<Product> list = new ArrayList<>();
        list.add(p);
        when(categoryService.getCategoryUsingId(c.getId())).thenReturn(c);
        when(productRepo.findByCategory(c)).thenReturn(list);

        Assertions.assertEquals(list.size(),productService.findByCategoryId(c.getId()).size());
    }

    @Test
    public void deleteProductTest(){

        Category c = new Category(1,"cname","cdes",null);
        Product p = new Product(1,"pname","purl",123,"pdes",c);

        when(productRepo.findById(p.getProduct_id())).thenReturn(Optional.of(p));
        productService.deleteProduct(p.getProduct_id());

        verify(productRepo).deleteById(p.getProduct_id());

    }







}
