package codebenchers006.ecommerce.service;

import codebenchers006.ecommerce.dto.ProductDTO;
import codebenchers006.ecommerce.exception.CustomException;
import codebenchers006.ecommerce.model.Category;
import codebenchers006.ecommerce.model.Inventory;
import codebenchers006.ecommerce.model.Product;
import codebenchers006.ecommerce.repository.ProductRepo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private static final Logger logger = LogManager.getLogger(ProductService.class);

    @Autowired
    ProductRepo productRepo;

    @Autowired
    CategoryService categoryService;

    @Autowired
    InventoryService inventoryService;

    public Product createProduct(ProductDTO productDTO, Category category) {

        Product product = new Product();
        logger.info("Setting Product Data");
        product.setDescription(productDTO.getDescription());
        product.setName(productDTO.getName());
        product.setImageUrl(productDTO.getImageUrl());
        product.setPrice(productDTO.getPrice());
        product.setCategory(category);

        System.out.println(productDTO);

        Inventory inventory = new Inventory();
        inventory.setProduct(product);
        inventory.setTotalItems(productDTO.getTotalItems());

        logger.info("New product created successfully");
        productRepo.save(product);

        logger.info("Adding product to inventory list");
        inventoryService.addInventory(inventory);
        logger.info("Inventory Created for Product : "+inventory.getProduct().getName()+" with total items : "+inventory.getTotalItems());
        return product;
    }

    public List<Product> showProducts() {
        logger.info("Displaying product details");
        return productRepo.findAll();
    }

    public ProductDTO getProductDto(Product product){
        ProductDTO productDTO = new ProductDTO();
        productDTO.setDescription(product.getDescription());
        productDTO.setName(product.getName());
        productDTO.setImageUrl(product.getImageUrl());
        productDTO.setCategoryId(product.getCategory().getId());
        productDTO.setPrice(product.getPrice());

        logger.info("Displaying productDTO details");
        return productDTO;
    }

    public List<ProductDTO> getProducts() {
        List<Product> prodList = productRepo.findAll();
        List<ProductDTO> productDTOS = new ArrayList<>();
        for(Product product: prodList){
            productDTOS.add(getProductDto(product));
        }
        return productDTOS;
    }

    public Boolean findByIdExist(int productId){
        return productRepo.findById(productId).isPresent();
    }

    public void updateProduct(int product_id,ProductDTO productDTO) {
        logger.info("Updating product details");
        Category category = categoryService.getCategoryUsingId(productDTO.getCategoryId());
        if(findByIdExist(product_id)){
            Product product = new Product();
            product.setProduct_id(product_id);
            product.setName(productDTO.getName());
            product.setPrice(productDTO.getPrice());
            product.setDescription(productDTO.getDescription());
            product.setImageUrl(productDTO.getImageUrl());
            product.setCategory(category);
            productRepo.save(product);
            logger.info("Product updated successfully");

            Inventory inventory = inventoryService.findInventoryItemsByProduct(product);
            if(inventory!=null){
                inventory.setTotalItems(productDTO.getTotalItems());
                inventoryService.addInventory(inventory);
            }
        }
    }


    public void deleteProduct(int productId) {
        logger.info("Product id: {} has been deleted",productId);
        productRepo.deleteById(productId);
    }

    public Product findByProductId(int productId) {
        Optional<Product> productOptional=productRepo.findById(productId);
        if(productOptional.isEmpty()){
            throw new CustomException("Invalid product id");
        }
        else
            return productOptional.get();
    }

    public ProductDTO getUsingId(int productId) {
        Product product = productRepo.findById(productId).get();
        return getProductDto(product);
    }

    public Product findByProductName(String productName){
        Product product = productRepo.findByName(productName);
        if(product==null){
            throw new CustomException("No Product Found");
        }
        else{
            return product;
        }
    }

    public List<Product> findByCategoryId(int id) {

        Category category = categoryService.getCategoryUsingId(id);
        List<Product> product = productRepo.findByCategory(category);
        return product;
    }
}
