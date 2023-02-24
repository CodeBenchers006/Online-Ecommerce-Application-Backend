package codebenchers006.ecommerce.service;

import codebenchers006.ecommerce.dto.cart.AddToCartDto;
import codebenchers006.ecommerce.dto.cart.CartDto;
import codebenchers006.ecommerce.dto.cart.CartItemDto;
import codebenchers006.ecommerce.exception.CustomException;
import codebenchers006.ecommerce.model.Cart;
import codebenchers006.ecommerce.model.Product;
import codebenchers006.ecommerce.model.User;
import codebenchers006.ecommerce.repository.CartRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    CartRepo cartRepo;

    @Autowired
    ProductService productService;

    public void addToCart(AddToCartDto addToCartDto, User user) {
        //validate if the product id is valid
        Product product = productService.findByProductId(addToCartDto.getProductId());

        Cart cart = new Cart();
        cart.setProduct(product);
        cart.setUser(user);
        cart.setQuantity(addToCartDto.getQuantity());
        cart.setCreatedDate(new Date());

        //save the cart
        cartRepo.save(cart);
    }

    public CartDto listCartItems(User user) {
        List<Cart> cartList =cartRepo.findAllByUserOrderByCreatedDateDesc(user);

        List<CartItemDto> cartItems = new ArrayList<>();
        double totalCost=0;

        for(Cart cart : cartList){

            CartItemDto cartItemDto = new CartItemDto(cart);
            totalCost+=cartItemDto.getQuantity() * cart.getProduct().getPrice();
            cartItems.add(cartItemDto);
        }

        CartDto cartDto = new CartDto();
        cartDto.setTotalCost(totalCost);
        cartDto.setCartItemDtoList(cartItems );

        return cartDto;
    }

    public void deleteCartItem(int cartid, User user) {

        //check if the item id belongs to the user
        Optional<Cart> optionalCart = cartRepo.findById(cartid);
        if(optionalCart.isEmpty())
            throw new CustomException("Cart Id is invalid");

        Cart cart = optionalCart.get();
        if(cart.getUser() != user){
            throw new CustomException("Cart item does not below to user: "+cartid);
        }

        cartRepo.delete(cart);

    }
}
