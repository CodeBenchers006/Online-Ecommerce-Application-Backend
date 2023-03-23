package codebenchers006.ecommerce.service;

import codebenchers006.ecommerce.model.Category;
import codebenchers006.ecommerce.model.Product;
import codebenchers006.ecommerce.repository.CategoryRepo;
import codebenchers006.ecommerce.service.CategoryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.ref.PhantomReference;
import java.util.*;

import static org.mockito.Mockito.*;

@SpringBootTest
public class CategoryServiceTest {

    @Mock
    CategoryRepo categoryRepo;

    @InjectMocks
    CategoryService categoryService;


    @Test
    public void createCategoryTest(){

        Product p = new Product();
        Set<Product> products = new HashSet<>();
        products.add(p);
        Category category = new Category(1,"abc","def",products);
       categoryService.createCategory(category);

       verify(categoryRepo,times(1)).save(category);
    }

    @Test
    public void showCategoriesTest(){

        Product p = new Product();
        Set<Product> products = new HashSet<>();
        products.add(p);
        List<Category> categoryList = new ArrayList<>();
        Category category = new Category(1,"abc","def",products);

        categoryList.add(category);

        when(categoryRepo.findAll()).thenReturn(categoryList);


        Assertions.assertEquals(1,categoryService.showCategories().size());
    }

    @Test
    public void readCategoryTest(){

        Product p = new Product();
        Set<Product> products = new HashSet<>();
        products.add(p);
        Category category = new Category(1,"abc","def",products);


        String name = "abc";

        when(categoryRepo.findByCategoryName(name)).thenReturn(category);

        Assertions.assertEquals(category,categoryService.readCategory(name));
    }

    @Test
    public void readCategoryByIdTest(){

        Product p = new Product();
        Set<Product> products = new HashSet<>();
        products.add(p);
        Optional<Category> category = Optional.of(new Category(1, "abc", "def", products));


        int id=1;

        when(categoryRepo.findById(id)).thenReturn(category);

        Assertions.assertEquals(category,categoryService.readCategory(id));

        Assertions.assertEquals(category,Optional.of(categoryService.getCategoryUsingId(id)));

        Assertions.assertEquals(true,categoryService.findById(id));
    }
    @Test
    public void updateCategoryTest() {

        Product p = new Product();
        Set<Product> products = new HashSet<>();
        products.add(p);
        Category category = new Category(1, "abc", "def", products);

        when(categoryRepo.findById(1)).thenReturn(Optional.of(category));
        categoryService.updateCategory(1, category);

        verify(categoryRepo,times(1)).save(category);

    }

    @Test
    public void deleteCategoryTest(){

        Product p = new Product();
        Set<Product> products = new HashSet<>();
        products.add(p);
        Category category = new Category(1, "abc", "def", products);

        when(categoryRepo.save(category)).thenReturn(category);

       categoryService.deleteCategory(1);
       verify(categoryRepo,times(1)).deleteById(1);
    }
}
