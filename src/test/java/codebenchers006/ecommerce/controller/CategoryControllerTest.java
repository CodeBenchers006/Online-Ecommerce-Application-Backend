package codebenchers006.ecommerce.controller;

import codebenchers006.ecommerce.exception.CustomException;
import codebenchers006.ecommerce.model.Category;
import codebenchers006.ecommerce.response.ApiResponse;
import codebenchers006.ecommerce.service.CategoryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class CategoryControllerTest {

    @InjectMocks
    CategoryController categoryController;

    @Mock
    CategoryService categoryService;


    @Test
    void testCreateCategory(){

        Category mockCategory = new Category();
        Mockito.doNothing().when(categoryService).createCategory(Mockito.any(Category.class));

        ResponseEntity<ApiResponse> responseEntity = categoryController.createCategory(mockCategory);
        Mockito.verify(categoryService).createCategory(mockCategory);

        assertEquals(HttpStatus.CREATED,responseEntity.getStatusCode());
        assertEquals(new ApiResponse(true,"Category created successfully"),responseEntity.getBody());
    }

    @Test
    void testShowCategory(){
        // Mock the category service to return a list of categories
        List<Category> mockCategories = Arrays.asList(new Category(), new Category());
        Mockito.when(categoryService.showCategories()).thenReturn(mockCategories);

        // Call the controller method
        List<Category> result = categoryController.showCategory();

        // Verify that the category service was called
        Mockito.verify(categoryService).showCategories();

        // Verify the response body
        assertEquals(mockCategories, result);
    }

    @Test
    void testGetCategoryById(){

        int id=123;
        Category mockCategory= new Category();
        Mockito.when(categoryService.getCategoryUsingId(id)).thenReturn(mockCategory);
        Mockito.when(categoryService.findById(id)).thenReturn(true);

        ResponseEntity<Category> responseEntity= categoryController.getCategoryById(id);

        Mockito.verify(categoryService).getCategoryUsingId(id);
        Mockito.verify(categoryService).findById(id);

        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        assertEquals(mockCategory,responseEntity.getBody());
    }


    @Test
    void testGetCategoryById_NotFound(){

        int mockCategoryId = 123;
        Mockito.when(categoryService.findById(mockCategoryId)).thenReturn(false);

        // Call the controller method and assert that a CustomException is thrown
        Exception exception = assertThrows(CustomException.class,
                () -> categoryController.getCategoryById(mockCategoryId));
        assertEquals("Category Id not found", exception.getMessage());
    }

    @Test
    public void testUpdateCategory() {
        // Create a mock category object
        Category mockCategory = new Category();

        // Mock the category service to return true for findById method
        int mockCategoryId = 1;
        Mockito.when(categoryService.findById(mockCategoryId)).thenReturn(true);

        // Call the controller method with the mock category and ID
        ResponseEntity<ApiResponse> response = categoryController.updateCategory(mockCategoryId, mockCategory);

        // Verify that the category service updateCategory method was called with the correct parameters
        Mockito.verify(categoryService, Mockito.times(1)).updateCategory(mockCategoryId, mockCategory);

        // Assert that the response is a success with the expected message
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(new ApiResponse(true,"Category updated successfully"), response.getBody());
    }

    @Test
    void testUpdateCategoryById_NotFound(){

        int mockCategoryId = 123;
        Category category = new Category();
        Mockito.when(categoryService.findById(mockCategoryId)).thenReturn(false);


        ResponseEntity<ApiResponse> response = categoryController.updateCategory(mockCategoryId,category);

        Assertions.assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testDeleteCategory() {
        // Mock the category service to return true for findById method
        int mockCategoryId = 1;
        Mockito.when(categoryService.findById(mockCategoryId)).thenReturn(true);

        // Call the controller method with the mock category ID
        ResponseEntity<ApiResponse> response = categoryController.deleteCategory(mockCategoryId);

        // Verify that the category service deleteCategory method was called with the correct parameter
        Mockito.verify(categoryService, Mockito.times(1)).deleteCategory(mockCategoryId);

        // Assert that the response is a success with the expected message
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(new ApiResponse(true,"Category deleted successfully"), response.getBody());
    }

    @Test
    void testDeleteCategoryById_NotFound(){

        int mockCategoryId = 123;

        Mockito.when(categoryService.findById(mockCategoryId)).thenReturn(false);


        ResponseEntity<ApiResponse> response = categoryController.deleteCategory(mockCategoryId);

        Assertions.assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }









}
