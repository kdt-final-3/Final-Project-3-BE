package com.finalproject.recruit.exception.authorization;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthException extends RuntimeException{

    private ErrorCode errorCode;
    private String message;

    public AuthException(ErrorCode err) {
        this.errorCode = err;
    }

    @Override
    public String getMessage(){
        if(message == null){
            return errorCode.getMessage();
        }
        return "{" +
                "\"state\":" + "\"" + errorCode.getStatus() + "\"" + "\n" +
                "\t" + "\"message\":" + errorCode.getMessage() +
                "}";
    }
}
