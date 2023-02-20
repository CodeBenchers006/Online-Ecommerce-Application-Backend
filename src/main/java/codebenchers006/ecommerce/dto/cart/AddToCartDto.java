package codebenchers006.ecommerce.dto.cart;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AddToCartDto{

        private int id;
        private @NotNull int productId;
        private @NotNull int quantity;
}
