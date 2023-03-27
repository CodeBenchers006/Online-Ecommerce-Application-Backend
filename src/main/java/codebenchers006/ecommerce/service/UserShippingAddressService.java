package codebenchers006.ecommerce.service;

import codebenchers006.ecommerce.model.User;
import codebenchers006.ecommerce.model.UserShippingAddress;
import codebenchers006.ecommerce.repository.UserShippAddressRepo;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Log
@Service
public class UserShippingAddressService {

    @Autowired
    UserShippAddressRepo userShippAddressRepo;




    public UserShippingAddress saveAddressForUser(User user, UserShippingAddress data){

        if(userShippAddressRepo.findByUser(user)!=null){
            log.info("Updating exisiting address details");

            System.out.println("Name "+user.getName());
            String name = user.getName();
            UserShippingAddress oldInfo = userShippAddressRepo.findByUser(user);
             oldInfo.setCountry(data.getCountry());
             oldInfo.setUser(user);
             oldInfo.setFirstName(data.getFirstName());
             oldInfo.setLastName(data.getLastName());
             oldInfo.setAddress(data.getAddress());
             oldInfo.setCity(data.getCity());
             oldInfo.setContact(data.getContact());
             oldInfo.setApartment(data.getApartment());
             oldInfo.setState(data.getState());
             oldInfo.setPin(data.getPin());

            userShippAddressRepo.save(oldInfo);
            return oldInfo;
        }
        else{
            log.info("creating new address details");
            String name = user.getName();
            UserShippingAddress userShippingAddress = UserShippingAddress.builder()
                    .country(data.getCountry())
                    .user(user)
                    .firstName(data.getFirstName())
                    .lastName(data.getLastName())
                    .contact(data.getContact())
                    .address(data.getAddress())
                    .apartment(data.getApartment())
                    .city(data.getCity())
                    .state(data.getState())
                    .pin(data.getPin())
                    .build();
            userShippAddressRepo.save(userShippingAddress);

            return userShippingAddress;
        }

    }

    public UserShippingAddress findAddressUsingUser(User user) {
        return userShippAddressRepo.findByUser(user);
    }
}
