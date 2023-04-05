package com.finalproject.recruit.exception.applyManage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    RECRUIT_FORM_NOT_FOUND(HttpStatus.NOT_FOUND, "Requested RecruitForm Not Found"),
    APPLY_NOT_FOUND(HttpStatus.NOT_FOUND, "Requested Apply Not Found"),
    APPLICANTS_FOUND_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "Requested Applicants Found Falied"),
    RECRUIT_FROM_KEYWORD_EXTRACTION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "Requested Extract Keyword from RecruitForm Failed"),
    KEYWORD_SCORE_CALCULATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "Requested Keyword Score Calculation Failed"),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "Requested Member Not Found");

    private HttpStatus status;
    private String message;
}
