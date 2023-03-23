package codebenchers006.ecommerce.service;


import codebenchers006.ecommerce.dto.ResponseDto;
import codebenchers006.ecommerce.dto.user.SignUpDto;
import codebenchers006.ecommerce.model.AuthenticationToken;
import codebenchers006.ecommerce.model.User;
import codebenchers006.ecommerce.repository.UserRepo;
import codebenchers006.ecommerce.service.AuthenticationService;
import codebenchers006.ecommerce.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.mockito.Mockito.*;


@SpringBootTest
public class UserServiceTest {

   @Mock
    UserRepo userRepo;

   @InjectMocks
    UserService userService;


   @Mock
    AuthenticationService aserve;


   @Test
    public void signUpUserTest(){

       //Arrange
       SignUpDto signUpDto = new SignUpDto("Aditya","passss123","abc@gmail.com");
       User user = new User(101,signUpDto.getName(),signUpDto.getEmail(),signUpDto.getPassword());
       ResponseDto responseDto = new ResponseDto("success","New User is created");

       AuthenticationToken token = new AuthenticationToken(user);
       userRepo.save(user);

       //Act
       doNothing().when(aserve).saveConfirmationToken(token);
       userService.signUpUser(signUpDto);

       //Assert
       verify(userRepo,times(1)).save(user);
       verify(userRepo,times(1)).findByEmail(user.getEmail());
   }

  @Test
    public void findByUserIdTest(){

      User user = new User(101,"Aditya","abc@gmail.com","pass");

      when(userRepo.findById(user.getUser_id())).thenReturn(Optional.of(user));
      User u2 = userService.findUserById(user.getUser_id());
      //System.out.println(u2);

      Assertions.assertEquals(user,u2);

  }

//  @Test
//    public void signInUserTest(){
//
//      User user = new User(101,"Aditya","abc@gmail.com","pass");
//
//      SignInDto signInDto = new SignInDto(user.getEmail(),user.getPassword());
//
//      when(userRepo.findByEmail(signInDto.getEmail())).thenReturn(user);
//
//
//
//
//  }

}
