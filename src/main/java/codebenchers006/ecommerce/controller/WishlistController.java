package codebenchers006.ecommerce.controller;

import codebenchers006.ecommerce.dto.ProductDTO;
import codebenchers006.ecommerce.model.Product;
import codebenchers006.ecommerce.model.User;
import codebenchers006.ecommerce.model.Wishlist;
import codebenchers006.ecommerce.response.ApiResponse;
import codebenchers006.ecommerce.service.AuthenticationService;
import codebenchers006.ecommerce.service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/wishlist")
public class WishlistController {

    @Autowired
    WishlistService wishlistService;

    @Autowired
    AuthenticationService authenticationService;

    /*
    This method is use to save an item to the wishlist
    First we authenticate the user with the help of the token
    If the Authentication is successful, we then find the User using the token
    We then add the item to the wishlist
     */
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addToWishList(@RequestBody Product product, @RequestParam("token") String token){

        //authenticate the token
        authenticationService.authenticate(token);

        //find the user
        User user = authenticationService.getUserfromToken(token);

        //save item in the wishlist
        Wishlist wishlist = new Wishlist(user,product);

        wishlistService.createWishList(wishlist);

        return new ResponseEntity<>(new ApiResponse(true, "Item has been added to wishlist"), HttpStatus.OK);
    }



    //get all wishlist item for a user
    @GetMapping("/{token}")
    public ResponseEntity<List<ProductDTO>> getWishListItems(@PathVariable String token){

        //authenticate the token
        authenticationService.authenticate(token);

        //find the user
        User user = authenticationService.getUserfromToken(token);

        List<ProductDTO> productDTOS = wishlistService.getWishListItemsForUser(user);

        return new ResponseEntity<>(productDTOS,HttpStatus.OK);
    }

}
