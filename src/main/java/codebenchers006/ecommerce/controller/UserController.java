package codebenchers006.ecommerce.controller;

import codebenchers006.ecommerce.dto.ResponseDto;
import codebenchers006.ecommerce.dto.SignInDtoResponse;
import codebenchers006.ecommerce.dto.user.SignInDto;
import codebenchers006.ecommerce.dto.user.SignUpDto;
import codebenchers006.ecommerce.dto.user.UserDto;
import codebenchers006.ecommerce.model.AuthenticationToken;
import codebenchers006.ecommerce.model.User;
import codebenchers006.ecommerce.model.UserShippingAddress;
import codebenchers006.ecommerce.response.ApiResponse;
import codebenchers006.ecommerce.service.AuthenticationService;
import codebenchers006.ecommerce.service.UserService;
import codebenchers006.ecommerce.service.UserShippingAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    UserShippingAddressService userShippingAddressService;

    //SignUp

    @PostMapping("/signup")
    public ResponseDto signUpUser(@RequestBody SignUpDto signUpDto){
       return userService.signUpUser(signUpDto);
    }

    //SignIn
    @PostMapping("/signin")
    public SignInDtoResponse singInUser(@RequestBody SignInDto signInDto){
        return userService.signInUser(signInDto);
    }


    @GetMapping("/{token}")
    public UserDto getUserData(@PathVariable String token){

        AuthenticationToken authenticationToken = authenticationService.getAuthenticationData(token);
        User user = authenticationToken.getUser();
        UserDto userDto = UserDto.builder()
                .id(user.getUser_id())
                .name(user.getName())
                .email(user.getEmail())
                .build();
        return userDto;
    }

    @PostMapping("/usershippingaddress")
    public ResponseEntity<?> addUserShippingAddress(@RequestParam String token, @RequestBody UserShippingAddress data){


            User user = authenticationService.getUserfromToken(token);
            if(user!=null && userService.checkIfUserExists(user.getUser_id())){

                UserShippingAddress userShippingAddress = userShippingAddressService.saveAddressForUser(user,data);


                return new ResponseEntity<>(userShippingAddress,HttpStatus.OK);
        }
            else return new ResponseEntity<>(new ApiResponse(false,"User not found"),HttpStatus.NOT_FOUND);
    }

    @GetMapping("/showUserAddress")
    public ResponseEntity<?> showUserAddress(@RequestParam String token){
        User user = authenticationService.getUserfromToken(token);
        if(user!=null && userService.checkIfUserExists(user.getUser_id())){

            UserShippingAddress userShippingAddress = userShippingAddressService.findAddressUsingUser(user);
            return new ResponseEntity<>(userShippingAddress,HttpStatus.OK);
        }
        else return new ResponseEntity<>(new ApiResponse(false,"User not found"),HttpStatus.NOT_FOUND);
    }
}
