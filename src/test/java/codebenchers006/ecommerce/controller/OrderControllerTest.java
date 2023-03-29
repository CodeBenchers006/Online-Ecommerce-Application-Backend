package codebenchers006.ecommerce.controller;

import codebenchers006.ecommerce.dto.checkout.CheckoutDto;
import codebenchers006.ecommerce.dto.checkout.StripeResponse;
import codebenchers006.ecommerce.dto.order.OrderAdmin;
import codebenchers006.ecommerce.exception.AuthenticationFailException;
import codebenchers006.ecommerce.exception.CustomException;
import codebenchers006.ecommerce.model.Order;
import codebenchers006.ecommerce.model.User;
import codebenchers006.ecommerce.response.ApiResponse;
import codebenchers006.ecommerce.service.AuthenticationService;
import codebenchers006.ecommerce.service.OrderItemsService;
import codebenchers006.ecommerce.service.OrderService;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Predicates.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
public class OrderControllerTest {

    @InjectMocks
    OrderController orderController;

    @Mock
    OrderService orderService;

    @Mock
    OrderItemsService orderItemsService;

    @Mock
    AuthenticationService authenticationService;

    MockMvc mockMvc;

    @Test
    public void testCheckoutList() throws StripeException {
        // Setup
        String token = "sampleToken";
        List<CheckoutDto> checkoutDtoList = new ArrayList<>();
        CheckoutDto checkoutDto1 = new CheckoutDto();
        CheckoutDto checkoutDto2 = new CheckoutDto();
        checkoutDtoList.add(checkoutDto1);
        checkoutDtoList.add(checkoutDto2);
        Session session = new Session();
        session.setId("sampleSessionId");
        when(orderService.createSession(checkoutDtoList)).thenReturn(session);

        // Execution
        ResponseEntity<StripeResponse> response = orderController.checkoutList(token, checkoutDtoList);

        // Verification
        verify(orderService, times(1)).createSession(checkoutDtoList);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("sampleSessionId", response.getBody().getSessionId());
    }


    @Test
    public void testPlaceOrder() throws AuthenticationFailException {
        // Mock user and session ID
        String token = "testToken";
        String sessionId = "testSessionId";
        User user = new User();
        user.setUser_id(1);

        // Set up mock authenticationService and orderService behaviors
        when(authenticationService.getUserfromToken(token)).thenReturn(user);

        doAnswer((invocation) -> {
            User u = invocation.getArgument(0);
            String s = invocation.getArgument(1);
            if (u.getUser_id() == 1 && s.equals(sessionId)) {
                return true;
            }
            return false;
        }).when(orderService).placeOrder(user, sessionId);

        // Call controller method and check response
        ResponseEntity<ApiResponse> response = orderController.placeOrder(token, sessionId);
        ApiResponse apiResponse = response.getBody();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, apiResponse.isSuccess());
        assertEquals("Order has been placed", apiResponse.getMessage());
    }


    @Test
    public void testGetAllOrders() throws AuthenticationFailException {
        // create a mock user
        User user = new User();
        user.setUser_id(1);
        user.setEmail("test@example.com");

        // create a mock order list
        List<Order> orderList = new ArrayList<>();
        Order order1 = new Order();
        order1.setId(1);
        order1.setUser(user);
        Order order2 = new Order();
        order2.setId(2);
        order2.setUser(user);
        orderList.add(order1);
        orderList.add(order2);

        // mock the authentication service to return the mock user
        when(authenticationService.getUserfromToken(anyString())).thenReturn(user);

        // mock the order service to return the mock order list
        when(orderService.listOrders(any(User.class))).thenReturn(orderList);

        // call the getAllOrders method with a mock token
        ResponseEntity<List<Order>> response = orderController.getAllOrders("mock-token");

        // assert that the authentication service was called with the mock token
        verify(authenticationService).authenticate("mock-token");

        // assert that the order service was called with the mock user
        verify(orderService).listOrders(user);

        // assert that the response status is OK
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // assert that the response body is the mock order list
        assertEquals(orderList, response.getBody());
    }

    @Test
    public void testGetOrderById() throws AuthenticationFailException, CustomException {
        // mock dependencies
        AuthenticationService authenticationService = mock(AuthenticationService.class);
        OrderService orderService = mock(OrderService.class);



        // set up test data
        Integer orderId = 1;
        String token = "test-token";
        Order order = new Order();

        // mock authentication
        User user = new User();
        doNothing().when(authenticationService).authenticate(token);

        // mock order service
        when(orderService.getOrder(orderId)).thenReturn(order);

        // call the controller method
        ResponseEntity<Object> response = orderController.getOrderById(orderId, token);

        // assert the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        //assertEquals(order, response.getBody());
    }

    @Test
    public void testDisplayOrdersForAdmin() throws CustomException {
        List<OrderAdmin> orderList = new ArrayList<>();
        orderList.add(new OrderAdmin());
        orderList.add(new OrderAdmin());
        when(orderService.getAllOrders()).thenReturn(orderList);

        ResponseEntity<?> responseEntity = orderController.displayOrdersForAdmin();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(orderList, responseEntity.getBody());
    }

    @Test
    public void testDisplayOrdersForAdminNoOrders() throws CustomException {
        when(orderService.getAllOrders()).thenThrow(new CustomException("No orders found"));

        ResponseEntity<?> responseEntity = orderController.displayOrdersForAdmin();
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Order not found", responseEntity.getBody());
    }


}
