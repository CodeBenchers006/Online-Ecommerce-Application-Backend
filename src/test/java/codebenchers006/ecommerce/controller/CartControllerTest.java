package codebenchers006.ecommerce.controller;

import codebenchers006.ecommerce.dto.cart.AddToCartDto;
import codebenchers006.ecommerce.dto.cart.CartDto;
import codebenchers006.ecommerce.model.User;
import codebenchers006.ecommerce.response.ApiResponse;
import codebenchers006.ecommerce.service.AuthenticationService;
import codebenchers006.ecommerce.service.CartService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class CartControllerTest {

    @InjectMocks
    CartController controller;

    @Mock
    CartService cartService;

    @Mock
    AuthenticationService authenticationService;


    private MockMvc mockMvc;

    @Before("")
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void testAddItemToCart() throws Exception {
        // Mock the authentication service to return a user
        User mockUser = new User();
        Mockito.when(authenticationService.getUserfromToken(Mockito.anyString())).thenReturn(mockUser);

        // Mock the cart service to do nothing
        Mockito.doNothing().when(cartService).addToCart(Mockito.any(AddToCartDto.class), Mockito.any(User.class));

        // Mock the request body and query parameter
        AddToCartDto mockAddToCartDto = new AddToCartDto();
        String mockToken = "mockToken";

        // Call the controller method
        ResponseEntity<ApiResponse> responseEntity = controller.addItemtoCart(mockAddToCartDto, mockToken);

        // Verify that the authentication service was called with the token
        Mockito.verify(authenticationService).authenticate(mockToken);

        // Verify that the cart service was called with the request body and user
        Mockito.verify(cartService).addToCart(mockAddToCartDto, mockUser);

        // Verify the response status code and body
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertEquals(new ApiResponse(true, "Added item to the cart"), responseEntity.getBody());
    }

    @Test
    public void testGetItemsFromCart() throws Exception {
        // Mock the authentication service to return a user
        User mockUser = new User();
        Mockito.when(authenticationService.getUserfromToken(Mockito.anyString())).thenReturn(mockUser);

        // Mock the cart service to return a cart DTO
        CartDto mockCartDto = new CartDto();
        Mockito.when(cartService.listCartItems(Mockito.any(User.class))).thenReturn(mockCartDto);

        // Mock the query parameter
        String mockToken = "mockToken";

        // Call the controller method
        ResponseEntity<CartDto> responseEntity = controller.getItemsFromCart(mockToken);

        // Verify that the authentication service was called with the token
        Mockito.verify(authenticationService).authenticate(mockToken);

        // Verify that the cart service was called with the user
        Mockito.verify(cartService).listCartItems(mockUser);

        // Verify the response status code and body
        Assertions.assertNotEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertEquals(mockCartDto, responseEntity.getBody());
    }

    @Test
    public void testDeleteItemsFromCart() throws Exception {
        // Mock the authentication service to return a user
        User mockUser = new User();
        Mockito.when(authenticationService.getUserfromToken(Mockito.anyString())).thenReturn(mockUser);

        // Mock the query parameter and path variable
        String mockToken = "mockToken";
        int mockCartId = 123;

        // Call the controller method
        ResponseEntity<ApiResponse> responseEntity = controller.deleteItemsFromCart(mockToken, mockCartId);

        // Verify that the authentication service was called with the token
        Mockito.verify(authenticationService).authenticate(mockToken);

        // Verify that the cart service was called with the cart id and user
        Mockito.verify(cartService).deleteCartItem(mockCartId, mockUser);

        // Verify the response status code and body
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertEquals(new ApiResponse(true, "Product has been deleted from the cart"), responseEntity.getBody());
    }

}
