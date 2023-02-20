package codebenchers006.ecommerce.controller;

import codebenchers006.ecommerce.dto.ResponseDto;
import codebenchers006.ecommerce.dto.SignInDtoResponse;
import codebenchers006.ecommerce.dto.user.SignInDto;
import codebenchers006.ecommerce.dto.user.SignUpDto;
import codebenchers006.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

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
}
