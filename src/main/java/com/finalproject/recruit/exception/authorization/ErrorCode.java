package com.finalproject.recruit.exception.authorization;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "Access Information Invalid "),
    INVALID_HEADER(HttpStatus.UNAUTHORIZED, "Header Information Invalid "),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED,"Access Information Expired"),
    EMPTY_HEADER(HttpStatus.BAD_REQUEST, "Security Information Not Found"),
    EMPTY_CLAMIS(HttpStatus.INTERNAL_SERVER_ERROR, "Security Information Not Found");

    private HttpStatus status;
    private String message;


}
