package com.finalproject.recruit.exception.keep;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    RECRUIT_FORM_NOT_FOUND(HttpStatus.NOT_FOUND, "RecruitForm not found"),
    APPLY_NOT_FOUND(HttpStatus.NOT_FOUND, "Requested Apply Not Found"),
    APPLICANTS_NOT_FOUND(HttpStatus.NOT_FOUND, "Requested Applicants Not Found"),
    UNABLE_TO_PROCESS_REQUEST(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to Process Request"),
    FAIL_CANCEL_DROP_APPLICANTS(HttpStatus.INTERNAL_SERVER_ERROR, "Fail to Cancel Drop Applicants");
    private HttpStatus status;
    private String message;
}
