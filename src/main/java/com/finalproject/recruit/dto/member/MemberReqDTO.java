package com.finalproject.recruit.dto.member;

import lombok.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@AllArgsConstructor
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

    /**
     * 로그인 요청 DTO
     */
    @Getter
    @Setter
    public static class Login {

        private String memberEmail;

        private String password;

        public UsernamePasswordAuthenticationToken toAuthentication() {
            return new UsernamePasswordAuthenticationToken(memberEmail, password);
        }

    }

    /**
     * 이메일 중복 체크 요청 DTO
     */
    @Getter
    @Setter
    public static class EmailValidate {

        private String memberEmail;

    }

    /**
     * 비밀번호 재설정 요청 DTO
     */
    @Getter
    @Setter
    public static class ResetPassword {

        private String memberEmail;

        private String newPassword;

        private String passwordCheck;

    }

    /**
     * 기업 정보 변경 요청 DTO
     */
    @Getter
    @Setter
    public static class Edit {

        private String password;

        private String memberPhone;

        private String ceoName;

    }
}
