package codebenchers006.ecommerce.service;

import codebenchers006.ecommerce.model.Inventory;
import codebenchers006.ecommerce.model.Product;
import codebenchers006.ecommerce.model.Sales;
import codebenchers006.ecommerce.repository.SalesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SalesService {

    @Autowired
    SalesRepository salesRepository;

    public List<Sales> getSalesList() {

        return salesRepository.findAll();
    }

    public void addSales(Sales sales){

        salesRepository.save(sales);
    }

    public Sales findSaleItemsByProduct(Product product){

        return salesRepository.findByProduct(product);
    }
}
