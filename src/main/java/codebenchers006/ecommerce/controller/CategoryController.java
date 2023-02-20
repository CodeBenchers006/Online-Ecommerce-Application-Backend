package codebenchers006.ecommerce.controller;

import codebenchers006.ecommerce.exception.CustomException;
import codebenchers006.ecommerce.model.Category;
import codebenchers006.ecommerce.response.ApiResponse;
import codebenchers006.ecommerce.service.CategoryService;
import codebenchers006.ecommerce.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/category")
public class CategoryController {

    private static final Logger logger = LogManager.getLogger(CategoryController.class);

    @Autowired
    CategoryService categoryService;

    /*
    This end point is used to add new categories
     */
    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createCategory(@RequestBody Category category){
        categoryService.createCategory(category);
        logger.info("New Category is created");
        return new ResponseEntity<>(new ApiResponse(true,"Category created successfully"),HttpStatus.CREATED);
    }

    @GetMapping("/list")
    public List<Category> showCategory(){
        return categoryService.showCategories();
    }


    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable int id){
        Category category = categoryService.getCategoryUsingId(id);
        if(!categoryService.findById(id)){
            throw new CustomException("Category Id not found");
        }
        else
            return new ResponseEntity<>(category,HttpStatus.OK);
    }

    /*
    This endpoint is used to update the existing category using the category id
    Parameter - category_id
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> updateCategory(@PathVariable int id, @RequestBody Category category ){
        if(categoryService.findById(id)){
            categoryService.updateCategory(id, category);
            logger.info("Category with Id : {} has been updated", id);
            return new ResponseEntity<>(new ApiResponse(true,"Category updated successfully"),HttpStatus.OK);
        }
        else{
            logger.error("Invalid Category Id");
            return new ResponseEntity<>(new ApiResponse(false,"Category ID not found "),HttpStatus.NOT_FOUND);
        }


    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable int id){
        if(categoryService.findById(id)){
            categoryService.deleteCategory(id);
            logger.info("Category with Id : {} has been deleted", id);
            return new ResponseEntity<>(new ApiResponse(true,"Category deleted successfully"),HttpStatus.OK);
        }
        else{
            logger.error("Invalid Category Id");
            return new ResponseEntity<>(new ApiResponse(false,"Category ID not found "),HttpStatus.NOT_FOUND);
        }

    }
}
