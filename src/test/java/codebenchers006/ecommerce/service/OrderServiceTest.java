package codebenchers006.ecommerce.service;

import codebenchers006.ecommerce.model.UserShippingAddress;
import codebenchers006.ecommerce.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class OrderServiceTest {

    @Mock
    OrderRepository orderRepository;

    @InjectMocks
    OrderService orderService;

    @Mock
    CartService cartService;

    @Mock
    InventoryService inventoryService;

    @Mock
    SalesService salesService;

    @Mock
    UserShippingAddressService userShippingAddressService;



}
