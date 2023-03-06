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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    OrderService orderService;

    @Autowired
    OrderItemsService orderItemsService;

    //Stripe checkout API
    @PostMapping("/create-checkout-session")
    public ResponseEntity<StripeResponse> checkoutList(@RequestParam("token")String token,@RequestBody List<CheckoutDto> checkoutDtoList) throws StripeException {

        Session session = orderService.createSession(checkoutDtoList);
        StripeResponse stripeResponse = new StripeResponse(session.getId());
        return new ResponseEntity<>(stripeResponse, HttpStatus.OK);

    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> placeOrder(@RequestParam("token") String token, @RequestParam("sessionId")String sessionId) throws AuthenticationFailException {
        if(sessionId != null){
            authenticationService.authenticate(token);
            User user = authenticationService.getUserfromToken(token);
            orderService.placeOrder(user,sessionId);
            return new ResponseEntity<>(new ApiResponse(true,"Order has been placed"),HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(new ApiResponse(false, "Session Id not found"),HttpStatus.BAD_REQUEST);
    }


    //get all orders
    @GetMapping("/listAll")
    public ResponseEntity<List<Order>> getAllOrders(@RequestParam("token") String token) throws AuthenticationFailException{

        //Authenticate
        authenticationService.authenticate(token);

       //retreive user
        User user = authenticationService.getUserfromToken(token);

        //get orders
        List<Order> orderDtoList = orderService.listOrders(user);

        return new ResponseEntity<>(orderDtoList,HttpStatus.OK);

    }


    //get orderitems for an order
    @GetMapping("/{id}")
    public ResponseEntity<Object> getOrderById(@PathVariable("id")Integer id,@RequestParam("token")String token) throws AuthenticationFailException {

        authenticationService.authenticate(token);
        try {
            Order order = orderService.getOrder(id);
            return new ResponseEntity<>(order,HttpStatus.OK);
        }
        catch( CustomException e) {
            return new ResponseEntity<>("Order not found", HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/displayOrders")
    public ResponseEntity<?> displayOrdersForAdmin(){

        try{
            List<OrderAdmin> orderList = orderService.getAllOrders();

            return new ResponseEntity<>(orderList,HttpStatus.OK);
        }
        catch(CustomException e){
            return new ResponseEntity<>("Order not found",HttpStatus.NOT_FOUND);
        }
    }



}
