package codebenchers006.ecommerce.dto.order;

import codebenchers006.ecommerce.model.OrderItem;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderAdmin {

    private int orderId;
    private String customerName;
    private String productName;

    private String orderDate;
    private String address;
    private String status;
}
