package codebenchers006.ecommerce.service;

import codebenchers006.ecommerce.dto.cart.AddToCartDto;
import codebenchers006.ecommerce.dto.cart.CartDto;
import codebenchers006.ecommerce.exception.CustomException;
import codebenchers006.ecommerce.model.Cart;
import codebenchers006.ecommerce.model.Product;
import codebenchers006.ecommerce.model.User;
import codebenchers006.ecommerce.repository.CartRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@SpringBootTest
public class CartServiceTest {

    @InjectMocks
    CartService cartService;

    @Mock
    CartRepo cartRepo;

    @Mock
    ProductService productService;

    @Test
    void testAddtoCart(){

        // create mock AddToCartDto and User objects
        AddToCartDto addToCartDto = new AddToCartDto();
        addToCartDto.setProductId(1);
        addToCartDto.setQuantity(2);
        User user = new User();

        // create mock Product object
        Product product = new Product();
        product.setProduct_id(1);
        product.setName("Test Product");
        product.setPrice(10.0);
        when(productService.findByProductId(1)).thenReturn(product);


        // call the addToCart method
        cartService.addToCart(addToCartDto, user);

        // verify that the cartRepo.save method was called with a Cart object
        ArgumentCaptor<Cart> cartCaptor = ArgumentCaptor.forClass(Cart.class);
        verify(cartRepo).save(cartCaptor.capture());
        Cart savedCart = cartCaptor.getValue();
        Assertions.assertEquals(product, savedCart.getProduct());
        Assertions.assertEquals(user, savedCart.getUser());
        Assertions.assertEquals(2, savedCart.getQuantity());
    }


    @Test
    public void testListCartItems() {

        User user = new User();

        Product p = new Product();
        p.setName("test Product");
        p.setPrice(10);

        Cart cart = new Cart();
        cart.setCart_id(1);
        cart.setProduct(p);
        cart.setUser(user);
        cart.setQuantity(2);
        cart.setCreatedDate(new Date());

        List<Cart> cartList = Arrays.asList(cart);
        when(cartRepo.findAllByUserOrderByCreatedDateDesc(user)).thenReturn(cartList);

        CartDto cartDto=cartService.listCartItems(user);

        Assertions.assertEquals(1,cartDto.getCartItemDtoList().size());
    }

    @Test
    public void testDeleteCartItem() {
        // create mock User object
        User user = new User();
        Product p = new Product();
        p.setName("test Product");
        p.setPrice(10);

        // create mock Cart object
        Cart cart = new Cart();
        cart.setCart_id(1);
        cart.setProduct(p);
        cart.setUser(user);
        cart.setQuantity(2);
        cart.setCreatedDate(new Date());

        Mockito.when(cartRepo.findById(cart.getCart_id())).thenReturn(Optional.of(cart));

        // call the deleteCartItem method
        cartService.deleteCartItem(cart.getCart_id(), user);

        // verify that the cartRepo.delete method was called with the correct Cart object
        verify(cartRepo).delete(cart);
    }


    @Test
    public void testDeleteCartItems() {


        // create a test user ID
        int userId = 1234;

        // call the method to be tested
        cartService.deleteCartItems(userId);

        // verify that the deleteAll() method of the mock CartRepo was called
        verify(cartRepo, times(1)).deleteAll();
    }

    @Test
    public void testDeleteUserCartItems() {


        // create a test user
        User user = new User(1,"abc","testuser", "password");

        // call the method to be tested
        cartService.deleteUserCartItems(user);

        // verify that the deleteByUser() method of the mock CartRepo was called with the correct argument
        verify(cartRepo, times(1)).deleteByUser(user);
    }
}
