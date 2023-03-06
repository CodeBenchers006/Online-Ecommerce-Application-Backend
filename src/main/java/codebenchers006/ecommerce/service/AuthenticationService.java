package codebenchers006.ecommerce.service;

import codebenchers006.ecommerce.exception.CustomException;
import codebenchers006.ecommerce.model.AuthenticationToken;
import codebenchers006.ecommerce.model.User;
import codebenchers006.ecommerce.repository.TokenRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AuthenticationService {

    @Autowired
    TokenRepo tokenRepo;

    public void saveConfirmationToken(AuthenticationToken authenticationToken) {
        tokenRepo.save(authenticationToken);
    }

    public AuthenticationToken getToken(User user) {
        return tokenRepo.findByUser(user);
    }

    public User getUserfromToken(String token){
        AuthenticationToken authenticationToken = tokenRepo.findByToken(token);
        if(Objects.isNull(authenticationToken)){
            return null;
        }
        else
            return authenticationToken.getUser();
    }

    public void authenticate(String token){
        if(Objects.isNull(token)){
            throw new CustomException("Token not Present");
        }

        if(Objects.isNull(getUserfromToken(token))){
            throw new CustomException("Token not valid");
        }
    }

    public AuthenticationToken getAuthenticationData(String token){
        return tokenRepo.findByToken(token);
    }
}
