package com.finalproject.recruit.exception.recruit;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    RECRUIT_FORM_NOT_FOUND(HttpStatus.NOT_FOUND, "Requested RecruitForm Not Found"),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "Requested Member Not Found"),
    FAIL_TO_EDIT_RECRUITFORM(HttpStatus.INTERNAL_SERVER_ERROR, "Fail to Edit RecruitForm"),
    FAIL_TO_REGIST_RECRUITFORM(HttpStatus.INTERNAL_SERVER_ERROR, "Fail to Regist RecruitForm"),
    FAIL_TO_DELETE_RECRUITFORM(HttpStatus.INTERNAL_SERVER_ERROR, "Fail to Delete RecruitForm"),
    UNABLE_TO_GET_RECRUITFORM(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to Get RecruitForm");
    private HttpStatus status;
    private String message;
}
