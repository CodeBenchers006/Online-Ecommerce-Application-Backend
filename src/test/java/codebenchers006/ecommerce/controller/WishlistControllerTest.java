package codebenchers006.ecommerce.controller;

import codebenchers006.ecommerce.dto.ProductDTO;
import codebenchers006.ecommerce.model.Product;
import codebenchers006.ecommerce.model.User;
import codebenchers006.ecommerce.model.Wishlist;
import codebenchers006.ecommerce.response.ApiResponse;
import codebenchers006.ecommerce.service.AuthenticationService;
import codebenchers006.ecommerce.service.WishlistService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class WishlistControllerTest {

    @InjectMocks
    WishlistController wishlistController;

    @Mock
    WishlistService wishlistService;

    @Mock
    AuthenticationService authenticationService;


    @Test
    public void testAddToWishList() {
        // Mock the authentication service
        when(authenticationService.getUserfromToken("test_token")).thenReturn(new User());

        // Create a product for the test
        Product product = new Product();
        product.setName("Test Product");
        product.setPrice(100.00);

        // Call the addToWishList method
        ResponseEntity<ApiResponse> response = wishlistController.addToWishList(product, "test_token");

        // Verify that the wishlist was created
        verify(wishlistService).createWishList(any(Wishlist.class));

        // Verify that the response is correct
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody().isSuccess());
        assertEquals("Item has been added to wishlist", response.getBody().getMessage());
    }

    @Test
    public void testGetWishListItems() {
        // Mock the authentication service
        when(authenticationService.getUserfromToken("test_token")).thenReturn(new User());

        // Create a list of products for the test
        List<ProductDTO> products = new ArrayList<>();
        ProductDTO product1 = new ProductDTO();
        product1.setName("Test Product 1");
        product1.setPrice(100.00);
        ProductDTO product2 = new ProductDTO();
        product2.setName("Test Product 2");
        product2.setPrice(200.00);
        products.add(product1);
        products.add(product2);

        // Mock the wishlistService to return the list of products
        when(wishlistService.getWishListItemsForUser(any(User.class))).thenReturn(products);

        // Call the getWishListItems method
        ResponseEntity<List<ProductDTO>> response = wishlistController.getWishListItems("test_token");

        // Verify that the wishlistService was called with the correct user
        verify(wishlistService).getWishListItemsForUser(any(User.class));

        // Verify that the response is correct
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(products, response.getBody());
    }
}
