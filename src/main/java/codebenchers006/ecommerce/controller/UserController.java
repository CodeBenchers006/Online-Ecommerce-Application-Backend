package codebenchers006.ecommerce.controller;

import codebenchers006.ecommerce.dto.ResponseDto;
import codebenchers006.ecommerce.dto.SignInDtoResponse;
import codebenchers006.ecommerce.dto.user.SignInDto;
import codebenchers006.ecommerce.dto.user.SignUpDto;
import codebenchers006.ecommerce.dto.user.UserDto;
import codebenchers006.ecommerce.model.AuthenticationToken;
import codebenchers006.ecommerce.model.User;
import codebenchers006.ecommerce.service.AuthenticationService;
import codebenchers006.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    AuthenticationService authenticationService;

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
}
