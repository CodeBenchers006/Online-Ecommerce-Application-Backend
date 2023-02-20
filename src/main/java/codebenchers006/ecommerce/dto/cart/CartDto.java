package codebenchers006.ecommerce.dto.cart;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CartDto {

    private List<CartItemDto> cartItemDtoList;
    private double totalCost;




}
