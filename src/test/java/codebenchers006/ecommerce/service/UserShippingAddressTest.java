package codebenchers006.ecommerce.service;

import codebenchers006.ecommerce.model.User;
import codebenchers006.ecommerce.model.UserShippingAddress;
import codebenchers006.ecommerce.repository.UserShippAddressRepo;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserShippingAddressTest {

    @InjectMocks
    UserShippingAddressService userShippingAddressService;

    @Mock
    UserShippAddressRepo userShippAddressRepo;

    @Test
    public void testSaveAddressForUser() {
        // Create a mock User object
        User mockUser = new User(1,"testuser", "testpassword", "testrole");

        // Create a mock UserShippingAddress object
        UserShippingAddress mockAddress = new UserShippingAddress();
        mockAddress.setCountry("USA");
        mockAddress.setUser(mockUser);
        mockAddress.setFirstName("John");
        mockAddress.setLastName("Doe");
        mockAddress.setAddress("123 Main St");
        mockAddress.setCity("Anytown");
        mockAddress.setContact("555-1234");
        mockAddress.setApartment("Apt 2");
        mockAddress.setState("CA");
        mockAddress.setPin("12345");

        // Set up the mock behavior for the userShippAddressRepo.findByUser() method to return the mock UserShippingAddress object
        when(userShippAddressRepo.findByUser(mockUser)).thenReturn(mockAddress);

        // Call the method to be tested with the mock User and UserShippingAddress objects and assert that it returns the updated UserShippingAddress object
        UserShippingAddress updatedAddress = new UserShippingAddress();
        updatedAddress.setCountry("Canada");
        updatedAddress.setUser(mockUser);
        updatedAddress.setFirstName("Jane");
        updatedAddress.setLastName("Doe");
        updatedAddress.setAddress("456 Maple St");
        updatedAddress.setCity("Othertown");
        updatedAddress.setContact("555-5678");
        updatedAddress.setApartment("Apt 3");
        updatedAddress.setState("BC");
        updatedAddress.setPin("67890");


        UserShippingAddress result = userShippingAddressService.saveAddressForUser(mockUser, updatedAddress);

        assertEquals(updatedAddress.getCountry(), result.getCountry());
        assertEquals(updatedAddress.getUser(), result.getUser());
        assertEquals(updatedAddress.getFirstName(), result.getFirstName());
        assertEquals(updatedAddress.getLastName(), result.getLastName());
        assertEquals(updatedAddress.getAddress(), result.getAddress());
        assertEquals(updatedAddress.getCity(), result.getCity());
        assertEquals(updatedAddress.getContact(), result.getContact());
        assertEquals(updatedAddress.getApartment(), result.getApartment());
        assertEquals(updatedAddress.getState(), result.getState());
        assertEquals(updatedAddress.getPin(), result.getPin());
    }

    @Test
    public void testFindAddressUsingUser() {
        // create a user object to use as input parameter
        User user = new User();
        user.setUser_id(1);
        user.setName("John Doe");

        // create a user shipping address object to use as output
        UserShippingAddress address = new UserShippingAddress();
        address.setAddress_id(1);
        address.setUser(user);
        address.setAddress("123 Main St");
        address.setCity("Anytown");
        address.setState("CA");
        address.setCountry("USA");
        address.setPin("12345");

        // define the behavior of the mock userShippAddressRepo object
        when(userShippAddressRepo.findByUser(user)).thenReturn(address);

        // call the method being tested
        UserShippingAddress result = userShippingAddressService.findAddressUsingUser(user);

        // assert that the result matches the expected output
        assertEquals(address, result);

        // verify that the mock object was called with the expected input parameter
        verify(userShippAddressRepo).findByUser(user);
    }
}
