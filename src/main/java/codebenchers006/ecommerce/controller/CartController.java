package codebenchers006.ecommerce.controller;

import codebenchers006.ecommerce.dto.cart.AddToCartDto;
import codebenchers006.ecommerce.dto.cart.CartDto;
import codebenchers006.ecommerce.model.User;
import codebenchers006.ecommerce.response.ApiResponse;
import codebenchers006.ecommerce.service.AuthenticationService;
import codebenchers006.ecommerce.service.CartService;
import codebenchers006.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    CartService cartService;

    @Autowired
    AuthenticationService authenticationService;


    //Add item to cart
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addItemtoCart(@RequestBody AddToCartDto addToCartDto, @RequestParam("token") String token){

        //authenticate the token
        authenticationService.authenticate(token);

        //find the user
        User user = authenticationService.getUserfromToken(token);

        cartService.addToCart(addToCartDto,user);

        return new ResponseEntity<>(new ApiResponse(true,"Added item to the cart"), HttpStatus.OK);

    }


    //Get Cart items
    @GetMapping("/items")
    public ResponseEntity<CartDto> getItemsFromCart(@RequestParam("token") String token){

        //authenticate the token
        authenticationService.authenticate(token);

        //find the user
        User user = authenticationService.getUserfromToken(token);

        CartDto cartDto = cartService.listCartItems(user);
        return new ResponseEntity<>(cartDto,HttpStatus.OK);

    }



//    //Delete item from cart
    @DeleteMapping("/delete/{cartid}")
    public ResponseEntity<ApiResponse> deleteItemsFromCart(@RequestParam("token") String token,@PathVariable int cartid){
        //authenticate the token
        authenticationService.authenticate(token);

        //find the user
        User user = authenticationService.getUserfromToken(token);

        cartService.deleteCartItem(cartid,user);

        return new ResponseEntity<>(new ApiResponse(true,"Product has been deleted from the cart"),HttpStatus.OK);

    }



}
