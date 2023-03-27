package codebenchers006.ecommerce.service;

import codebenchers006.ecommerce.model.Product;
import codebenchers006.ecommerce.model.Sales;
import codebenchers006.ecommerce.repository.SalesRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

@SpringBootTest
public class SalesServiceTest {

    @InjectMocks
    SalesService salesService;

    @Mock
    SalesRepository salesRepository;


    @Test
    void testGetSalesList(){

        List<Sales> salesList = Arrays.asList(
                new Sales(1,new Product(),10),
                new Sales(2,new Product(),10)
        );

        when(salesRepository.findAll()).thenReturn(salesList);
        List<Sales> actualSalesList = salesService.getSalesList();

        verify(salesRepository,times(1)).findAll();
        Assertions.assertEquals(salesList,actualSalesList);

    }

    @Test
    void testAddSales(){
        Sales mockSales = new Sales(1, new Product(), 10);

        // Call the method to be tested
        salesService.addSales(mockSales);

        // Verify that the method is called once with the mock Sales object
        verify(salesRepository, times(1)).save(mockSales);
    }


    @Test
    public void testFindSaleItemsByProduct() {
        // Create a mock Product object
        Product mockProduct = new Product();

        // Create a mock Sales object
        Sales mockSales = new Sales(1, mockProduct, 10);

        // Set up the mock behavior for salesRepository.findByProduct() method
        when(salesRepository.findByProduct(mockProduct)).thenReturn(mockSales);

        // Call the method to be tested
        Sales actualSales = salesService.findSaleItemsByProduct(mockProduct);

        // Verify that the method is called once with the mock Product object
        verify(salesRepository, times(1)).findByProduct(mockProduct);

        // Verify that the returned Sales object is the same as the mock Sales object
        Assertions.assertEquals(mockSales, actualSales);
    }
}
