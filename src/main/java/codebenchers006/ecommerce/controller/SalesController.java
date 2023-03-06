package codebenchers006.ecommerce.controller;

import codebenchers006.ecommerce.model.Inventory;
import codebenchers006.ecommerce.model.Product;
import codebenchers006.ecommerce.model.Sales;
import codebenchers006.ecommerce.response.ApiResponse;
import codebenchers006.ecommerce.service.ProductService;
import codebenchers006.ecommerce.service.SalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/sales")
public class SalesController {

    @Autowired
    SalesService salesService;

    @Autowired
    ProductService productService;

    @GetMapping("/checkAllSales")
    public ResponseEntity<?> getAllSales(){

        try{
            List<Sales> salesList = salesService.getSalesList();

            if(salesList.isEmpty()){
                return new ResponseEntity<>("Sales list is empty", HttpStatus.NOT_FOUND);
            }
            else{
                return new ResponseEntity<>(salesList, HttpStatus.OK);
            }
        }
        catch(Exception e){
            return new ResponseEntity<>(new ApiResponse(false,"No Sales info found"),HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/getSalesByProduct/{product_id}")
    public ResponseEntity<?> getSalesByProduct(int product_id){

        try{
            Product product = productService.findByProductId(product_id);
            Sales sales = salesService.findSaleItemsByProduct(product);
            return new ResponseEntity<>(sales,HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(new ApiResponse(false,"not found"),HttpStatus.NOT_FOUND);
        }
    }
}
