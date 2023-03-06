package codebenchers006.ecommerce.controller;

import codebenchers006.ecommerce.dto.ProductDTO;

import codebenchers006.ecommerce.exception.CustomException;
import codebenchers006.ecommerce.model.Category;
import codebenchers006.ecommerce.model.Product;
import codebenchers006.ecommerce.repository.CategoryRepo;

import codebenchers006.ecommerce.response.ApiResponse;
import codebenchers006.ecommerce.service.ProductService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin
@RestController
@RequestMapping("/product")
public class ProductController {



    @Autowired
    ProductService productService;

    @Autowired
    CategoryRepo categoryRepo;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createProduct(@RequestBody ProductDTO productDTO){
        if(categoryRepo.findById(productDTO.getCategoryId()).isPresent()){
            Category category=categoryRepo.findById(productDTO.getCategoryId()).get();
            productService.createProduct(productDTO,category);
            return new ResponseEntity<>(new ApiResponse(true,"Product has been created"),HttpStatus.CREATED);
        }
        else
            return new ResponseEntity<>(new ApiResponse(false,"Category id not found"),HttpStatus.NOT_FOUND);

    }

    @GetMapping("/listAll")
    public List<Product> showProducts(){
        return productService.showProducts();
    }

    @GetMapping("/list")
    public List<ProductDTO> getProducts(){
        return productService.getProducts();
    }

    /*
    Note - Only Product Details are changed not the Category Details
     */
    @PutMapping("/update/{product_id}")
    public ResponseEntity<ApiResponse> updateProduct(@PathVariable int product_id,@RequestBody ProductDTO productDTO){

        if(productService.findById(product_id)){
            productService.updateProduct(product_id,productDTO);
            System.out.println("Product data updated");
            return new ResponseEntity<>(new ApiResponse(true, "Product details updated successfully"),HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(new ApiResponse(false, "Product Id not found"),HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/delete/{product_id}")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable int product_id){
        System.out.println(productService.findById(product_id));
        if(productService.findById(product_id)){
            productService.deleteProduct(product_id);
            return new ResponseEntity<>(new ApiResponse(true, "Product deleted successfully"),HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(new ApiResponse(false, "Product Id not found"),HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{product_id}")
    public ResponseEntity<ProductDTO> getById(@PathVariable int product_id){
        if(productService.findById(product_id)){
            ProductDTO product = productService.getUsingId(product_id);
            return new ResponseEntity<>(product,HttpStatus.OK);
        }
        else
           throw new CustomException("Product Id not found");
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Product> getByProductName(@PathVariable String name){
        Product product = productService.findByProductName(name);
        if(product !=null){
            return new ResponseEntity<>(product,HttpStatus.OK);
        }
        else{
            throw new CustomException("Product not found");
        }

    }

    @GetMapping("/category/{id}")
    public ResponseEntity<List<Product>> getByCategoryId(@PathVariable int id){
        List<Product> product = productService.findByCategoryId(id);
        return new ResponseEntity<>(product,HttpStatus.OK);
    }



}

