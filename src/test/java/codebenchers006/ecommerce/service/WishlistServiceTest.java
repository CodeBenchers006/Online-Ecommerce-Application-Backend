package codebenchers006.ecommerce.service;

import codebenchers006.ecommerce.dto.ProductDTO;
import codebenchers006.ecommerce.model.Product;
import codebenchers006.ecommerce.model.User;
import codebenchers006.ecommerce.model.Wishlist;
import codebenchers006.ecommerce.repository.WishlistRepo;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class WishlistServiceTest {


    @InjectMocks
    WishlistService wishlistService;

    @Mock
    WishlistRepo wishlistRepo;

    @Mock
    ProductService productService;


    @Test
    public void testCreateWishList() {
        // create a sample Wishlist object to use as input parameter
        Wishlist wishlist = new Wishlist();
        wishlist.setId(1);
        wishlist.setUser(new User());


        // call the method being tested
        wishlistService.createWishList(wishlist);

        // verify that the save method of the mock object was called with the expected input parameter
        verify(wishlistRepo).save(wishlist);
    }


    @Test
    public void testGetWishListItemsForUser() {
        // create a sample User object to use as input parameter
        User user = new User();


        // create a sample Wishlist object to use in the mock result of the findAllByUserOrderByCreatedDateDesc method
        Wishlist wishlist1 = new Wishlist();
        wishlist1.setId(1);
        wishlist1.setProduct(new Product());
        Wishlist wishlist2 = new Wishlist();
        wishlist2.setId(2);
        wishlist2.setProduct(new Product());

        // create a sample ProductDTO object to use in the mock result of the getProductDto method
        ProductDTO productDTO1 = new ProductDTO();
        productDTO1.setDtoId(1);
        productDTO1.setName("Product 1");
        ProductDTO productDTO2 = new ProductDTO();
        productDTO2.setDtoId(2);
        productDTO2.setName("Product 2");

        // create a sample list of ProductDTO objects to use as expected output
        List<ProductDTO> expectedProducts = Arrays.asList(productDTO1, productDTO2);

        // configure the mock objects to return the expected results
        when(wishlistRepo.findAllByUserOrderByCreatedDateDesc(user)).thenReturn(Arrays.asList(wishlist1, wishlist2));
        when(productService.getProductDto(wishlist1.getProduct())).thenReturn(productDTO1);
        when(productService.getProductDto(wishlist2.getProduct())).thenReturn(productDTO2);

        // call the method being tested
        List<ProductDTO> actualProducts = wishlistService.getWishListItemsForUser(user);

        // verify that the mock methods were called with the expected input parameters
        verify(wishlistRepo).findAllByUserOrderByCreatedDateDesc(user);
        verify(productService).getProductDto(wishlist1.getProduct());
        verify(productService).getProductDto(wishlist2.getProduct());

        // verify that the actual output matches the expected output
        assertEquals(expectedProducts, actualProducts);
    }

}


