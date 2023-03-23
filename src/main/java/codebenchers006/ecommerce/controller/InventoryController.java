package codebenchers006.ecommerce.controller;


import codebenchers006.ecommerce.model.Inventory;
import codebenchers006.ecommerce.model.Product;
import codebenchers006.ecommerce.response.ApiResponse;
import codebenchers006.ecommerce.service.InventoryService;
import codebenchers006.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    InventoryService inventoryService;

    @Autowired
    ProductService productService;

    @GetMapping("/checkAll")
    public ResponseEntity<?> getAllInventories(){

        try{
            List<Inventory> inventoryList = inventoryService.getInventoryList();

            if(inventoryList.isEmpty()){
                return new ResponseEntity<>("Inventory List Empty",HttpStatus.NOT_FOUND);
            }
            else{
                return new ResponseEntity<>(inventoryList, HttpStatus.OK);
            }
        }
        catch(Exception e){
            return new ResponseEntity<>(new ApiResponse(false,"No inventory found"),HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/getByProduct/{product_id}")
    public ResponseEntity<?> getByProduct(@PathVariable int product_id){

        try{
            Product product = productService.findByProductId(product_id);
            Inventory inventory = inventoryService.findInventoryItemsByProduct(product);
            return new ResponseEntity<>(inventory,HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(new ApiResponse(false,"not found"),HttpStatus.NOT_FOUND);
        }
    }

}
