package codebenchers006.ecommerce.service;

import codebenchers006.ecommerce.dto.cart.CartDto;
import codebenchers006.ecommerce.dto.cart.CartItemDto;
import codebenchers006.ecommerce.dto.checkout.CheckoutDto;
import codebenchers006.ecommerce.dto.order.OrderAdmin;
import codebenchers006.ecommerce.exception.CustomException;
import codebenchers006.ecommerce.model.Order;
import codebenchers006.ecommerce.model.OrderItem;
import codebenchers006.ecommerce.model.User;
import codebenchers006.ecommerce.repository.OrderItemsRepository;
import codebenchers006.ecommerce.repository.OrderRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Value("${BASE_URL}")
    private String baseUrl;

    @Value("${STRIPE_SECRET_KEY}")
    private String secretKey;

    @Autowired
    CartService cartService;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderItemsRepository orderItemsRepository;


    public Session createSession(List<CheckoutDto> checkoutDtoList) throws StripeException {

        //success and failure urls

        String successUrl = baseUrl + "home/checkout/payment/success";
        String failureUrl = baseUrl + "home/checkout/payment/failed";
        Stripe.apiKey = secretKey;

        List<SessionCreateParams.LineItem> sessionItemList = new ArrayList<>();
        for(CheckoutDto checkoutDto: checkoutDtoList){
            sessionItemList.add(createSessionLineItem(checkoutDto));
        }
       SessionCreateParams params = SessionCreateParams.builder()
               .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
               .setMode(SessionCreateParams.Mode.PAYMENT)
               .setCancelUrl(failureUrl)
               .setSuccessUrl(successUrl)
               .addAllLineItem(sessionItemList)
               .build();

        return Session.create(params);
    }

    private SessionCreateParams.LineItem createSessionLineItem(CheckoutDto checkoutDto) {

        return SessionCreateParams.LineItem.builder()
                .setPriceData(createPriceData(checkoutDto))
                .setQuantity(Long.parseLong(String.valueOf(checkoutDto.getQuantity())))
                .build();
    }

    private SessionCreateParams.LineItem.PriceData createPriceData(CheckoutDto checkoutDto) {
        return SessionCreateParams.LineItem.PriceData.builder()
                .setCurrency("inr")
                .setUnitAmount((long)checkoutDto.getPrice()*100)
                .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                        .setName(checkoutDto.getProductName())
                        .build())
                .build();
    }

    public void placeOrder(User user, String sessionId) {

        if(sessionId!=null){
            // first  get cart items for the user
            CartDto cartDto = cartService.listCartItems(user);
            List<CartItemDto> cartItemDtoList = cartDto.getCartItemDtoList();

            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
            String currenDate = formatter.format(date);

            // create the order and save it
            Order neworder = Order.builder()
                    .createdDate(currenDate)
                    .sessionId(sessionId)
                    .user(user)
                    .totalPrice(cartDto.getTotalCost())
                    .build();

            orderRepository.save(neworder);

            for(CartItemDto cartItemDto: cartItemDtoList){

                OrderItem orderItem = OrderItem.builder()
                        .createdDate(currenDate)
                        .price(cartItemDto.getProduct().getPrice())
                        .product(cartItemDto.getProduct())
                        .quantity(cartItemDto.getQuantity())
                        .order(neworder)
                        .build();


                orderItemsRepository.save(orderItem);
            }

            cartService.deleteCartItems(user.getUser_id());
        }

    }

    public List<Order> listOrders(User user) {
        return orderRepository.findAllByUserOrderByIdDesc(user);
    }

    public Order getOrder(Integer orderId) throws CustomException {
        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isPresent()) {
            return order.get();
        }
        throw new CustomException("Order not found");
    }

    public List<OrderAdmin> getAllOrders(){

        List<OrderAdmin> orderAdminList = new ArrayList<>();

        List<OrderItem> orderList = orderItemsRepository.findAll();

        for(OrderItem order: orderList){

            Date d1 = new Date(order.getCreatedDate());
            Date d2 = new Date();

            SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
            String currenDate = formatter.format(d2);

            String status="";

            if(d1.getTime()<d2.getTime()){
                status= "Dispatched, will be delivered in 7 business days";
            }
            else{
                status="Delivered on "+currenDate;
            }

            OrderAdmin data = OrderAdmin.builder()
                    .orderId(order.getId())
                    .customerName(order.getOrder().getUser().getName())
                    .productName(order.getProduct().getName())
                    .orderDate(order.getCreatedDate())
                    .address("xyz")
                    .status(status)
                    .build();

            orderAdminList.add(data);


        }




        return orderAdminList;
    }
}
