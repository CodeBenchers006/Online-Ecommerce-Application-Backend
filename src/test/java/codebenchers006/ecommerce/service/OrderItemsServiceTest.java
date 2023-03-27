package codebenchers006.ecommerce.service;

import codebenchers006.ecommerce.model.OrderItem;
import codebenchers006.ecommerce.model.Product;
import codebenchers006.ecommerce.repository.OrderItemsRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class OrderItemsServiceTest {


    @InjectMocks
    OrderItemsService orderItemsService;

    @Mock
    OrderItemsRepository orderItemsRepository;

    @Test
    void testAddOrderedProducts(){


        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(new Product());
        orderItem.setQuantity(2);
        orderItem.setPrice(10.0);

        orderItemsService.addOrderedProducts(orderItem);

        verify(orderItemsRepository, times(1)).save(orderItem);
    }
}
