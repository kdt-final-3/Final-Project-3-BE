package com.finalproject.recruit.exception.recruit;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RecruitException extends RuntimeException{

    private ErrorCode errorCode;
    private String message;

    public RecruitException(ErrorCode err){
        this.errorCode = err;
    }

    @Override
    public String getMessage(){
        if(message == null)
            if(message == null){
                return "{" +
                        "\"state\":" + "\"" + errorCode.getStatus() + "\"" + "\n" +
                        "\t" + "\"message\":" + errorCode.getMessage() +
                        "}";
            }
        return "{" +
                "\"state\":" + "\"" + errorCode.getStatus() + "\"" + "\n" +
                "\t" + "\"message\":" + message +
                "}";
    }
}
