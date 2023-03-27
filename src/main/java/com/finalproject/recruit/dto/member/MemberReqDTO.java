package com.finalproject.recruit.dto.member;

import lombok.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

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
}
