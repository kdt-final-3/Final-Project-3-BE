package com.finalproject.recruit.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class UserResDTO {
    /**
     * JWT Token Provider를 위한 임시 DTO 설정
     */
    @Builder
    @Getter
    @AllArgsConstructor
    public static class TokenInfo{
        private String grantType;
        private String accessToken;
        private String refreshToken;
        private Long refreshTokenExpirationTime;
    }
}
