package codebenchers006.ecommerce.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApiResponse {

    private final boolean success;
    private final String message;


    public String getTimeStamp(){
        return LocalDateTime.now().toString();
    }

}
