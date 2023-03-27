package codebenchers006.ecommerce.service;

import codebenchers006.ecommerce.exception.CustomException;
import codebenchers006.ecommerce.model.AuthenticationToken;
import codebenchers006.ecommerce.model.User;
import codebenchers006.ecommerce.repository.TokenRepo;
import codebenchers006.ecommerce.service.AuthenticationService;
import codebenchers006.ecommerce.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AuthenticationServiceTest {

    @InjectMocks
    AuthenticationService authenticationService;

    @Mock
    TokenRepo tokenRepo;

    @Mock
    UserService userService;

    @Test
    void saveConfirmationTokenTest(){

        AuthenticationToken token = new AuthenticationToken();
        token.setTokenId(1);
        token.setToken("abc");
        token.setUser(new User(1,"abc","email","password"));

        authenticationService.saveConfirmationToken(token);

        verify(tokenRepo,times(1)).save(token);

    }

    @Test
    void getTokenTest(){

        User u =new User(1,"abc","email","password");
        AuthenticationToken token = new AuthenticationToken();
        token.setTokenId(1);
        token.setToken("abc");
        token.setUser(u);

        when(tokenRepo.findByUser(u)).thenReturn(token);
        AuthenticationToken ex =authenticationService.getToken(u);

        Assertions.assertEquals(ex,token);

    }

    @Test
    void getUserFromTokenTest(){

        User u =new User(1,"abc","email","password");
        AuthenticationToken token = new AuthenticationToken();
        token.setTokenId(1);
        token.setToken("abc");
        token.setUser(u);

        when(tokenRepo.findByToken(token.getToken())).thenReturn(token);

        User ex = authenticationService.getUserfromToken(token.getToken());

        Assertions.assertEquals(ex,u);
    }

   @Test
    void getAuthDataTest(){

       User u =new User(1,"abc","email","password");

       AuthenticationToken token = new AuthenticationToken();
       token.setTokenId(1);
       token.setToken("abc");
       token.setUser(u);

       when(tokenRepo.findByToken(token.getToken())).thenReturn(token);
       AuthenticationToken ex = authenticationService.getAuthenticationData(token.getToken());
       Assertions.assertEquals(ex,token);
   }

    @Test
    public void testAuthenticateWithNullToken() {
        // Call the method to be tested with a null token and assert that it throws a CustomException
        assertThrows(CustomException.class, () -> authenticationService.authenticate(null));
    }

    @Test
    public void testAuthenticateWithInvalidToken() {
        // Create a mock token string
        String mockToken = "mock-token";


        // Call the method to be tested with the mock token string and assert that it throws a CustomException
        assertThrows(CustomException.class, () -> authenticationService.authenticate(mockToken));
    }

}
