package codebenchers006.ecommerce.service;

import codebenchers006.ecommerce.dto.cart.CartDto;
import codebenchers006.ecommerce.dto.cart.CartItemDto;
import codebenchers006.ecommerce.dto.checkout.CheckoutDto;
import codebenchers006.ecommerce.dto.order.OrderAdmin;
import codebenchers006.ecommerce.exception.CustomException;
import codebenchers006.ecommerce.model.*;
import codebenchers006.ecommerce.repository.OrderItemsRepository;
import codebenchers006.ecommerce.repository.OrderRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@SpringBootTest
public class OrderServiceTest {

    @Mock
    OrderRepository orderRepository;

    @InjectMocks
    OrderService orderService;

    @Mock
    CartService cartService;

    @Mock
    InventoryService inventoryService;

    @Mock
    SalesService salesService;

    @Mock
    UserShippingAddressService userShippingAddressService;

    @Mock
    OrderItemsRepository orderItemsRepository;


    @Mock
    Stripe stripe;

    @Mock
    Session session;




    @Test
    public void testPlaceOrder(){

        User user = new User();
        user.setUser_id(1);
        String sessionId ="abc123";

        List<CartItemDto> cartItemDtoList = new ArrayList<>();
        Product product = new Product();
        product.setProduct_id(1);
        product.setName("Test Product");
        product.setPrice(10);

        CartItemDto cartItemDto = new CartItemDto();
        cartItemDto.setProduct(product);
        cartItemDto.setQuantity(2);
        cartItemDtoList.add(cartItemDto);

        CartDto cartDto = new CartDto();
        cartDto.setCartItemDtoList(cartItemDtoList);
        cartDto.setTotalCost(20);

        UserShippingAddress userShippingAddress = new UserShippingAddress();
        userShippingAddress.setAddress("Test Address");
        userShippingAddress.setApartment("Test Apartment");
        userShippingAddress.setCity("Test City");
        userShippingAddress.setState("Test State");
        userShippingAddress.setCountry("Test Country");
        userShippingAddress.setPin("Test Pin");

        //Mocking
        when(cartService.listCartItems(user)).thenReturn(cartDto);
        when(userShippingAddressService.findAddressUsingUser(user)).thenReturn(userShippingAddress);
        when(inventoryService.findInventoryItemsByProduct(product)).thenReturn(new Inventory());
        when(salesService.findSaleItemsByProduct(product)).thenReturn(new Sales());

        //call method to be tested
        orderService.placeOrder(user,sessionId);

        //verify the dependencies
        verify(cartService).listCartItems(user);
        verify(userShippingAddressService).findAddressUsingUser(user);
        verify(orderRepository).save(any(Order.class));
        verify(inventoryService,times(2)).findInventoryItemsByProduct(product);
        verify(inventoryService).addInventory(any(Inventory.class));
        verify(salesService,times(2)).findSaleItemsByProduct(product);
        verify(orderItemsRepository, times(cartItemDtoList.size())).save(any(OrderItem.class));
        verify(cartService).deleteCartItems(user.getUser_id());

    }

    @Test
    public void testListOrders() {
        // create a user
        User user = new User();
        user.setName("testuser");
        user.setPassword("testpass");
        user.setEmail("testuser@example.com");

        // create some orders for the user
        Order order1 = new Order();
        order1.setUser(user);
        order1.setTotalPrice(10.0);
        orderRepository.save(order1);

        Order order2 = new Order();
        order2.setUser(user);
        order2.setTotalPrice(20.0);
        orderRepository.save(order2);

        // mock the orderRepository to return the orders when findAllByUserOrderByIdDesc is called
        when(orderRepository.findAllByUserOrderByIdDesc(user)).thenReturn(Arrays.asList(order2, order1));

        // call the listOrders method
        List<Order> orders = orderService.listOrders(user);

        // assert that the correct orders are returned and in the correct order
        assertEquals(2, orders.size());
        assertEquals(order2, orders.get(0));
        assertEquals(order1, orders.get(1));
    }


    @Test
    public void testGetOrder() throws CustomException {
        Order order = new Order();
        order.setId(1);
        order.setTotalPrice(100.0);
        when(orderRepository.findById(1)).thenReturn(Optional.of(order));

        Order result = orderService.getOrder(1);

        assertNotNull(result);
        assertEquals(order, result);
    }

    @Test
    public void testGetOrderNotFound() {
        when(orderRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(CustomException.class, () -> orderService.getOrder(1));
    }


    @Test
    public void testGetAllOrders() {
        // create some sample data for testing
        User user = new User();
        user.setName("John Doe");
        Product product = new Product();
        product.setName("Test Product");
        Order order = new Order();
        order.setUser(user);
        OrderItem orderItem = new OrderItem();
        orderItem.setId(1);
        orderItem.setOrder(order);
        orderItem.setProduct(product);
        orderItem.setCreatedDate("2022-03-23");
        orderItem.setDeliveryAddress("123 Main St");
        List<OrderItem> orderList = Arrays.asList(orderItem);
        when(orderItemsRepository.findAll()).thenReturn(orderList);

        // invoke the method under test
        List<OrderAdmin> result = orderService.getAllOrders();

        // assert that the result is as expected
        assertEquals(1, result.size());
        OrderAdmin orderAdmin = result.get(0);
        assertEquals(1, orderAdmin.getOrderId());
        assertEquals("John Doe", orderAdmin.getCustomerName());
        assertEquals("Test Product", orderAdmin.getProductName());
        assertEquals("2022-03-23", orderAdmin.getOrderDate());
        assertEquals("123 Main St", orderAdmin.getAddress());
        assertTrue(orderAdmin.getStatus().startsWith("Dispatched"));
    }




}
