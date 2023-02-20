package codebenchers006.ecommerce.dto.cart;

import codebenchers006.ecommerce.model.Cart;
import codebenchers006.ecommerce.model.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CartItemDto {
    private int cartid;
    private int quantity;
    private Product product;

    public CartItemDto(Cart cart){
        this.cartid=cart.getCart_id();
        this.quantity=cart.getQuantity();
        this.product=cart.getProduct();
    }
}
