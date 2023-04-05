package com.finalproject.recruit.exception.applicant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    EMAIL_NOT_FOUND(HttpStatus.NOT_FOUND, "Requested Email Not Found"),
    RECRUIT_FORM_NOT_FOUND(HttpStatus.NOT_FOUND, "Requested RecruitForm Not Found"),
    FAIL_TO_SUBMIT_APPLICANT(HttpStatus.INTERNAL_SERVER_ERROR, "Requested Application Submit Failed"),
    UNABLE_TO_PROCESS_REQUEST(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to Process Request");

    private HttpStatus status;
    private String message;
}
