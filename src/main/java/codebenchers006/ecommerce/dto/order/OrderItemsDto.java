package codebenchers006.ecommerce.dto.order;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemsDto {

    private @NotNull double price;
    private @NotNull int quantity;
    private @NotNull int orderId;
    private @NotNull int productId;
}
