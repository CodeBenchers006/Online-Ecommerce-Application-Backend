package codebenchers006.ecommerce.controller;

import codebenchers006.ecommerce.model.Product;
import codebenchers006.ecommerce.model.Sales;
import codebenchers006.ecommerce.response.ApiResponse;
import codebenchers006.ecommerce.service.ProductService;
import codebenchers006.ecommerce.service.SalesService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class SalesControllerTest {

    @InjectMocks
    SalesController salesController;

    @Mock
    SalesService salesService;

    @Mock
    ProductService productService;


    @Test
    public void testGetAllSales() throws Exception {
        List<Sales> salesList = new ArrayList<Sales>();
        salesList.add(new Sales());
        salesList.add(new Sales());

        when(salesService.getSalesList()).thenReturn(salesList);

        ResponseEntity<?> response = salesController.getAllSales();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(salesList, response.getBody());

        verify(salesService, times(1)).getSalesList();
        verifyNoMoreInteractions(salesService);
    }

    @Test
    public void testGetAllSalesEmpty() throws Exception {
        List<Sales> salesList = new ArrayList<Sales>();

        when(salesService.getSalesList()).thenReturn(salesList);

        ResponseEntity<?> response = salesController.getAllSales();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Sales list is empty", response.getBody());

        verify(salesService, times(1)).getSalesList();
        verifyNoMoreInteractions(salesService);
    }

    @Test
    public void testGetAllSalesException() throws Exception {
        when(salesService.getSalesList()).thenThrow(new RuntimeException());

        ResponseEntity<?> response = salesController.getAllSales();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(new ApiResponse(false,"No Sales info found"), response.getBody());

        verify(salesService, times(1)).getSalesList();
        verifyNoMoreInteractions(salesService);
    }

    @Test
    void testGetSalesByProduct(){

        Product product = new Product();
        Sales sales=new Sales();

        when(productService.findByProductId(1)).thenReturn(product);
        when(salesService.findSaleItemsByProduct(product)).thenReturn(sales);

        ResponseEntity<?> response=salesController.getSalesByProduct(1);

        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(sales,response.getBody());

        verify(productService,times(1)).findByProductId(1);
        verify(salesService,times(1)).findSaleItemsByProduct(product);
        verifyNoMoreInteractions(productService,salesService);
    }


    @Test
    public void testGetSalesByProductException() throws Exception {
        when(productService.findByProductId(1)).thenThrow(new RuntimeException());

        ResponseEntity<?> response = salesController.getSalesByProduct(1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(new ApiResponse(false,"not found"), response.getBody());

        verify(productService, times(1)).findByProductId(1);
        verifyNoMoreInteractions(productService, salesService);
    }


}
