package com.finalproject.recruit.exception.authorization;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "Invalid Access Information"),
    INVALID_HEADER(HttpStatus.UNAUTHORIZED, "Invalid Header Information"),
    EMPTY_HEADER(HttpStatus.BAD_REQUEST, "Security Information Not Found");

    private HttpStatus status;
    private String message;


}
