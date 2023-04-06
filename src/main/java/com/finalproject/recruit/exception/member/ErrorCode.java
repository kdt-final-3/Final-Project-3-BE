package com.finalproject.recruit.exception.member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "Requested Member Not Found"),
    INCORRECT_PASSWORD(HttpStatus.UNAUTHORIZED, "Password Incorrect"),
    INCORRECT_AUTH_NUM(HttpStatus.UNAUTHORIZED, "Authorization Number Incorrect"),
    MISMATCH_PASSWORD(HttpStatus.BAD_REQUEST, "Mismatch Password"),
    INVALID_TOKEN(HttpStatus.BAD_REQUEST, "Invalid Token Information"),
    ALREADY_EXIST(HttpStatus.CONFLICT, "Aleady Registed Email"),
    REDIS_SAVED_INFO_NOT_FOUND(HttpStatus.NOT_FOUND, "Redis Saved Data Not Found"),
    REDIS_AUTH_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "Requested TokenInfo from Redis Not Found"),
    REDIS_NUM_EXPIRED(HttpStatus.UNAUTHORIZED, "Requested AuthNum from Redis Expired"),
    REDIS_VERIFIED_FALID(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to Verified MemberInfo"),
    REDIS_REGIST_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to Regist MemberInfo to Redis"),
    REDIS_DELETE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to Delete MemberInfo from Redis"),
    REDIS_AUTH_LOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to Load TokenInfo from Redis"),
    REDIS_NUM_LOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to Load AuthNum from Redis"),
    SIGNUP_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "Requested Signup Failed"),
    LOGIN_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "Requested Login Failed"),
    LOGOUT_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "Requested Logout Failed"),
    NUMBER_GENERATE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "Requested Number Generation Failed"),
    MEMBER_DELETE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "Requested Member delete Failed"),
    PASSWORD_UPDATE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "Requested Password Update Failed"),
    AUTH_NUM_VALIDATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "Requested Authorization Member Validation Failed"),
    INFO_UPDATE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "Requested MemberInfo Update Failed"),
    EXTEND_TOKEN_TIME_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "Requested TokenTime Extend Failed"),
    UNABLE_TO_SEND_MESSAGE(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to Send Message"),
    UNABLE_TO_PROCESS_REQUEST(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to Process Request");

    private HttpStatus status;
    private String message;
}
