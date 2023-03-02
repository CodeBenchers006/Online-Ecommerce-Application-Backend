package codebenchers006.ecommerce.service;

import codebenchers006.ecommerce.dto.ResponseDto;
import codebenchers006.ecommerce.dto.SignInDtoResponse;
import codebenchers006.ecommerce.dto.user.SignInDto;
import codebenchers006.ecommerce.dto.user.SignUpDto;
import codebenchers006.ecommerce.exception.CustomException;
import codebenchers006.ecommerce.model.AuthenticationToken;
import codebenchers006.ecommerce.model.User;
import codebenchers006.ecommerce.repository.UserRepo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

@Service
public class UserService {

    private static final Logger logger = LogManager.getLogger(UserService.class);

    @Autowired
    UserRepo userRepo;

    @Autowired
    AuthenticationService aserve;


    @Transactional
    public ResponseDto signUpUser(SignUpDto signUpDto) {

        //check if user already exists
       if(Objects.nonNull(userRepo.findByEmail(signUpDto.getEmail()))){
           //user already exist
           logger.error("User already exist with email {}",signUpDto.getEmail());
           throw new CustomException("user already exists ");
       }

        //hash the password
       String encryptedPassword = signUpDto.getPassword();
       try{
           encryptedPassword = hashPassword(encryptedPassword);
       } catch (NoSuchAlgorithmException e) {
           e.printStackTrace();
       }

        //save the user
        User newUser = new User();
        newUser.setName(signUpDto.getName());
        newUser.setEmail(signUpDto.getEmail());
        newUser.setPassword(encryptedPassword);
        userRepo.save(newUser);

        //create the token
        final AuthenticationToken authenticationToken = new AuthenticationToken(newUser);
        aserve.saveConfirmationToken(authenticationToken);

        ResponseDto responseDto = new ResponseDto("success","New User is created");
        logger.info("Signup process is completed");
        return responseDto;
    }

    private String hashPassword(String encryptedPassword) throws NoSuchAlgorithmException {

        logger.info("Encrypting the password");

        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(encryptedPassword.getBytes());
        byte[] digest= md.digest();
        String hash = DatatypeConverter
                .printHexBinary(digest).toUpperCase();
        return hash;
    }

    public SignInDtoResponse signInUser(SignInDto signInDto) {

        logger.info("Sign In process Started");

        //find user by email
        User user = (User) userRepo.findByEmail(signInDto.getEmail());

        if(Objects.isNull(user)){
            logger.error("Invalid / Incorrect Email id");
            throw new CustomException("Authentication failed");
        }

        //hash the password
       try{
           if(!user.getPassword().equals(hashPassword(signInDto.getPassword()))){
               logger.error("Incorrect Password");
                throw new CustomException("Password is incorrect");
           }
       } catch (NoSuchAlgorithmException e) {
           e.printStackTrace();
       }

        //compare the password in db


        //if password is same, then retrieve the token and return the response
        AuthenticationToken authenticationToken=aserve.getToken(user);

       if(Objects.isNull(authenticationToken)){
           logger.error("Invalid token");
           throw new CustomException("Token is not present");
       }
        SignInDtoResponse response = new SignInDtoResponse("success", user.getName(),user.getEmail(),authenticationToken.getToken());
       logger.info("Sign In process completed");
        return response;
    }
}
