package com.finalproject.recruit.exception.recruit;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    RECRUIT_FORM_NOT_FOUND(HttpStatus.NOT_FOUND, "RecruitForm not found");
    private HttpStatus status;
    private String message;
}
