package codebenchers006.ecommerce.service;

import codebenchers006.ecommerce.dto.cart.CartDto;
import codebenchers006.ecommerce.dto.cart.CartItemDto;
import codebenchers006.ecommerce.dto.checkout.CheckoutDto;
import codebenchers006.ecommerce.dto.order.OrderAdmin;
import codebenchers006.ecommerce.exception.CustomException;
import codebenchers006.ecommerce.model.*;
import codebenchers006.ecommerce.repository.OrderItemsRepository;
import codebenchers006.ecommerce.repository.OrderRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.extern.java.Log;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Log
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

    @Autowired
    InventoryService inventoryService;

    @Autowired
    SalesService salesService;

    @Autowired
    UserShippingAddressService userShippingAddressService;


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

            UserShippingAddress userShippingAddress = userShippingAddressService.findAddressUsingUser(user);
            String address = userShippingAddress.getAddress()+", "+userShippingAddress.getApartment()+", "+userShippingAddress.getCity()+", "+userShippingAddress.getState()+", "+userShippingAddress.getCountry()+", "+userShippingAddress.getPin();


            // create the order and save it
            Order neworder = Order.builder()
                    .createdDate(currenDate)
                    .sessionId(sessionId)
                    .user(user)
                    .totalPrice(cartDto.getTotalCost())
                    .deliveryAddress(address)
                    .build();

            orderRepository.save(neworder);

            for(CartItemDto cartItemDto: cartItemDtoList){

                OrderItem orderItem = OrderItem.builder()
                        .createdDate(currenDate)
                        .price(cartItemDto.getProduct().getPrice())
                        .product(cartItemDto.getProduct())
                        .quantity(cartItemDto.getQuantity())
                        .order(neworder)
                        .deliveryAddress(address)
                        .build();


                Product prod = orderItem.getProduct();

                log.info("order placed");

                if(inventoryService.findInventoryItemsByProduct(prod)!=null){
                    log.info("Finding Inventory using product");
                    Inventory inventory = inventoryService.findInventoryItemsByProduct(prod);
                    log.info("Inventory found");
                    log.info("Updating total items left in the inventory after placing the order");
                    log.info("Total items for "+orderItem.getProduct().getName()+" was :"+inventory.getTotalItems());
                    log.info("Total quantity of "+orderItem.getProduct().getName()+" was ordered :"+orderItem.getQuantity());

                    int totalItemsUpdated=inventory.getTotalItems() - orderItem.getQuantity();

                    inventory.setTotalItems(totalItemsUpdated);
                    log.info("Total items for "+orderItem.getProduct().getName()+" is left now :"+inventory.getTotalItems());

                    log.info("updating inventory after order placed");
                    inventoryService.addInventory(inventory);

                }

                if(salesService.findSaleItemsByProduct(prod)==null){
                    log.info("Creating a new sale record for Product: "+prod.getName());

                    Sales sales = new Sales();
                    sales.setProduct(prod);
                    sales.setTotalSales(orderItem.getQuantity());

                    salesService.addSales(sales);

                }
                else{
                    log.info("Updating sale record for product :"+prod.getName());
                    Sales sales = salesService.findSaleItemsByProduct(prod);

                    int totalSales = sales.getTotalSales() + orderItem.getQuantity();
                    log.info("Total sale of "+prod.getName()+ " is : "+totalSales);
                    sales.setTotalSales(totalSales);

                    salesService.addSales(sales);
                }

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
            String currentDate = formatter.format(d2);

            String status="";

            if(d1.getTime()<d2.getTime()){
                status= "Dispatched, will be delivered in 7 business days";
            }
            else{
                status="Delivered on "+currentDate;
            }

            OrderAdmin data = OrderAdmin.builder()
                    .orderId(order.getId())
                    .customerName(order.getOrder().getUser().getName())
                    .productName(order.getProduct().getName())
                    .orderDate(order.getCreatedDate())
                    .address(order.getDeliveryAddress())
                    .status(status)
                    .build();

            orderAdminList.add(data);


        }




        return orderAdminList;
    }
}
