package com.finalproject.recruit.dto.member;

import com.finalproject.recruit.entity.Member;
import lombok.*;

@Getter @Setter
@ToString
public class MemberResDTO {

    @Builder @Getter
    @AllArgsConstructor
    public static class TokenInfo {

        // 인증정보 관련
        private String accessToken;
        private String refreshToken;
        private Long refreshTokenExpirationTime;

        // 회원정보 관련
        private String ceoName;
        private String companyName;
        private String memberEmail;
        private String companyNum;
        private String memberPhone;

        // 토큰 유효기간 연장시
        public TokenInfo(String acsToken, String rfToken, Long rfTokenExpiredTime){
            this.accessToken = acsToken;
            this.refreshToken = rfToken;
            this.refreshTokenExpirationTime = rfTokenExpiredTime;
        }

        // 로그인 시
        public TokenInfo(String acsToken, String rfToken, Long rfTokenExpiredTime, Member member){
            this.accessToken = acsToken;
            this.refreshToken = rfToken;
            this.refreshTokenExpirationTime = rfTokenExpiredTime;
            this.ceoName = member.getCeoName();
            this.companyName = member.getCompanyName();
            this.memberEmail = member.getMemberEmail();
            this.companyNum = member.getCompanyNum();
            this.memberPhone = member.getMemberPhone();
        }
    }
}
