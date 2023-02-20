package codebenchers006.ecommerce.dto.checkout;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CheckoutDto {

    private String productName;
    private int quantity;
    private double price;
    private long productId;
    private int userId;
}
