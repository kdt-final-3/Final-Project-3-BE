package com.finalproject.recruit.exception.notice;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    RECRUIT_FORM_NOT_FOUND(HttpStatus.NOT_FOUND, "Requested RecruitForm Not Found"),
    APPLY_NOT_FOUND(HttpStatus.NOT_FOUND, "Requested Apply Not Found"),
    APPLICANTS_NOT_FOUND(HttpStatus.NOT_FOUND, "Requested Applicants Not Found"),
    HISTORY_NOT_FOUND(HttpStatus.NOT_FOUND, "Requested MessageHistory Not Found"),
    INVALID_STEP(HttpStatus.BAD_REQUEST, "Requested Step is Invalid"),
    UNABLE_TO_SEND_MESSAGE(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to Send Message"),
    UNABLE_TO_ARCHIVE_HISTORY(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to Archive Sending History"),
    UNABLE_TO_PROCESS_REQUEST(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to Process Request");

    private HttpStatus status;
    private String message;
}
