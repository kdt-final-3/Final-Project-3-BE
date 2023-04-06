package com.finalproject.recruit.exception.applyManage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    RECRUIT_FORM_NOT_FOUND(HttpStatus.NOT_FOUND, "Requested RecruitForm Not Found"),
    APPLICANTS_NOT_FOUND(HttpStatus.NOT_FOUND, "Requested Applicants Not Found"),
    FAIL_CHECK_APPLICANT_DETAIL(HttpStatus.INTERNAL_SERVER_ERROR, "Fail Check Applicant Detail"),
    APPLICANTS_FOUND_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "Requested Applicants Found Falied"),
    RECRUIT_FROM_KEYWORD_EXTRACTION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "Requested Extract Keyword from RecruitForm Failed"),
    KEYWORD_SCORE_CALCULATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "Requested Keyword Score Calculation Failed"),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "Requested Member Not Found"),
    FAIL_CHANGE_APPLICANTS_PROCEDURE(HttpStatus.INTERNAL_SERVER_ERROR, "Failed Change Applicants Procedure"),
    FAIL_CHANGE_APPLICANT_PASS_OR_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "Failed Change Applicants Pass or Fail"),
    FAIL_COUNT_APPLICANT_AND_CHECK_PROCESS_AND_DAY(HttpStatus.INTERNAL_SERVER_ERROR, "Failed Count Applicants And Check Recruit Process And Process D-day"),
    FAIL_COMMENT_APPLICANT(HttpStatus.INTERNAL_SERVER_ERROR, "Failed Comment Applicant"),
    FAIL_WISH_APPLICANT(HttpStatus.INTERNAL_SERVER_ERROR, "Failed Comment Applicant"),
    FAIL_DROP_APPLICANT(HttpStatus.INTERNAL_SERVER_ERROR, "Failed Drop Applicant"),
    FAIL_SET_APPLICANT_MEETING_DAY(HttpStatus.INTERNAL_SERVER_ERROR, "Failed Set Applicant Meeting day"),
    FAIL_SET_CHECKING_APPLY_DOCS(HttpStatus.INTERNAL_SERVER_ERROR, "Failed Set Checking Apply Document");

    private HttpStatus status;
    private String message;
}
