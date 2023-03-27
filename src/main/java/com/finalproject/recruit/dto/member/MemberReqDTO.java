package com.finalproject.recruit.dto.member;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MemberReqDTO {

    /**
     * 회원가입 요청 DTO
     */
    @Getter
    @Setter
    public static class SignUp {

        private String memberEmail;

        private String password;

        private String memberPhone;

        private String companyNum;

        private String companyName;

        private String ceoName;

    }
}
