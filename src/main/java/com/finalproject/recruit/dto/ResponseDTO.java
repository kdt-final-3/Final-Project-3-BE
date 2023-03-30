package com.finalproject.recruit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class ResponseDTO<T> {
    private HttpStatus state;
    private String resultCode;
    private T result;

    public static <T> ResponseDTO<T> message(T result){
        if(result == null){
            return new ResponseDTO<>(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to Process Your Request", null);
        }
        else{
            return new ResponseDTO<>(HttpStatus.OK, "SUCCESS", result);
        }
    }
}
