package codebenchers006.ecommerce.controller;

import codebenchers006.ecommerce.dto.ResponseDto;
import codebenchers006.ecommerce.dto.SignInDtoResponse;
import codebenchers006.ecommerce.dto.user.SignInDto;
import codebenchers006.ecommerce.dto.user.SignUpDto;
import codebenchers006.ecommerce.dto.user.UserDto;
import codebenchers006.ecommerce.model.AuthenticationToken;
import codebenchers006.ecommerce.model.User;
import codebenchers006.ecommerce.model.UserShippingAddress;
import codebenchers006.ecommerce.repository.UserRepo;
import codebenchers006.ecommerce.response.ApiResponse;
import codebenchers006.ecommerce.service.AuthenticationService;
import codebenchers006.ecommerce.service.UserService;
import codebenchers006.ecommerce.service.UserShippingAddressService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserControllerTest {

    @InjectMocks
    UserController userController;

    @Mock
    UserService userService;
    @Mock
    AuthenticationService authenticationService;
    @Mock
    UserShippingAddressService userShippingAddressService;

    @Test
    public void testSignUpUser() {
        // Arrange
        SignUpDto signUpDto = new SignUpDto();
        ResponseDto expectedResponseDto = new ResponseDto("success", "User signed up successfully");
        when(userService.signUpUser(signUpDto)).thenReturn(expectedResponseDto);

        // Act
        ResponseDto actualResponseDto = userController.signUpUser(signUpDto);

        // Assert
        assertNotNull(actualResponseDto);
        assertEquals(expectedResponseDto.getStatus(), actualResponseDto.getStatus());
        assertEquals(expectedResponseDto.getMessage(), actualResponseDto.getMessage());


        verify(userService, times(1)).signUpUser(signUpDto);
    }

    @Test
    public void testSingInUser() {
        // Arrange
        SignInDto signInDto = new SignInDto("testuser", "testpassword");
        SignInDtoResponse expectedResponse = new SignInDtoResponse();
        when(userService.signInUser(signInDto)).thenReturn(expectedResponse);

        // Act
        SignInDtoResponse actualResponse = userController.singInUser(signInDto);

        // Assert
        assertNotNull(actualResponse);
        assertEquals(expectedResponse.getName(), actualResponse.getName());
        assertEquals(expectedResponse.getToken(), actualResponse.getToken());

        verify(userService, times(1)).signInUser(signInDto);
    }

    @Test
    public void testGetUserData() {
        // Create a mock user and authentication token
        User user = new User();
        user.setUser_id(1);
        user.setName("John");
        user.setEmail("john@example.com");

        AuthenticationToken authToken = new AuthenticationToken();
        authToken.setToken("token123");
        authToken.setUser(user);

        // Set up the mock authentication service to return the mock token

        when(authenticationService.getAuthenticationData(anyString())).thenReturn(authToken);

        // Create the controller instance and set its authentication service to the mock object


        // Make the request to the controller method
        UserDto response = userController.getUserData("token123");

        // Verify that the response is not null and has the expected values
        assertNotNull(response);
        assertEquals(user.getUser_id(), response.getId());
        assertEquals(user.getName(), response.getName());
        assertEquals(user.getEmail(), response.getEmail());
    }

    @Test
    public void testAddUserShippingAddress() {
        // Arrange
        String token = "token123";
        User user = new User();
        UserShippingAddress userShippingAddress = new UserShippingAddress();
        when(authenticationService.getUserfromToken(token)).thenReturn(user);
        when(userService.checkIfUserExists(user.getUser_id())).thenReturn(true);
        when(userShippingAddressService.saveAddressForUser(user, userShippingAddress)).thenReturn(userShippingAddress);

        // Act
        ResponseEntity<?> actualResponseEntity = userController.addUserShippingAddress(token, userShippingAddress);

        // Assert
        assertNotNull(actualResponseEntity);
        assertEquals(HttpStatus.OK, actualResponseEntity.getStatusCode());
        assertEquals(userShippingAddress, actualResponseEntity.getBody());

        verify(authenticationService, times(1)).getUserfromToken(token);
        verify(userService, times(1)).checkIfUserExists(user.getUser_id());
        verify(userShippingAddressService, times(1)).saveAddressForUser(user, userShippingAddress);
    }

    @Test
    public void testAddUserShippingAddress_userNotFound() {
        // Arrange
        String token = "token123";
        User user = new User();
        UserShippingAddress userShippingAddress = new UserShippingAddress();
        when(authenticationService.getUserfromToken(token)).thenReturn(user);
        when(userService.checkIfUserExists(user.getUser_id())).thenReturn(false);

        // Act
        ResponseEntity<?> actualResponseEntity = userController.addUserShippingAddress(token, userShippingAddress);

        // Assert
        assertNotNull(actualResponseEntity);
        assertEquals(HttpStatus.NOT_FOUND, actualResponseEntity.getStatusCode());
        assertEquals(new ApiResponse(false, "User not found"), actualResponseEntity.getBody());

        verify(authenticationService, times(1)).getUserfromToken(token);
        verify(userService, times(1)).checkIfUserExists(user.getUser_id());
        verifyZeroInteractions(userShippingAddressService);
    }

    @Test
    public void testShowUserAddress() {
        // Create a mock user and address
        User user = new User();


        UserShippingAddress address = new UserShippingAddress();


        // Set up the mock authentication service to return the mock user
        when(authenticationService.getUserfromToken(anyString())).thenReturn(user);

        // Set up the mock user service to return true for checkIfUserExists
        when(userService.checkIfUserExists(anyInt())).thenReturn(true);

        // Set up the mock user shipping address service to return the mock address
       ;
        when(userShippingAddressService.findAddressUsingUser(eq(user))).thenReturn(address);


        // Make the request to the controller method
        ResponseEntity<?> response = userController.showUserAddress("token123");

        // Verify that the response has status code 200 (OK)
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Verify that the response body is not null and is equal to the mock address
        assertNotNull(response.getBody());
        assertEquals(address, response.getBody());
    }

}
