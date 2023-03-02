package codebenchers006.ecommerce.service;

import codebenchers006.ecommerce.dto.checkout.CheckoutDto;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @Value("${BASE_URL}")
    private String baseUrl;

    @Value("${STRIPE_SECRET_KEY}")
    private String secretKey;


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

}
