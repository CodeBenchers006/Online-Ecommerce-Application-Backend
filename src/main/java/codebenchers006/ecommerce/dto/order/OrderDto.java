package codebenchers006.ecommerce.dto.order;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDto {

    private int id;
    private @NotNull int userId;

    private String deliveryAddress;
}
