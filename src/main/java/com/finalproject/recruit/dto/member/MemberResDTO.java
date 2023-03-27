package com.finalproject.recruit.dto.member;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MemberResDTO {
    
    @Builder
    @Getter
    @AllArgsConstructor
    public static class TokenInfo {

        private String grantType;

        private String accessToken;

        private String refreshToken;

        private Long refreshTokenExpirationTime;
    }
}
