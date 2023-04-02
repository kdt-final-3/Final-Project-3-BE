package com.finalproject.recruit.dto.member;

import lombok.*;

@Getter @Setter
@ToString
public class MemberResDTO {

    @Builder
    @Getter
    @AllArgsConstructor
    public static class TokenInfo {
        private String accessToken;
        private String refreshToken;
        private Long refreshTokenExpirationTime;

    }
}
