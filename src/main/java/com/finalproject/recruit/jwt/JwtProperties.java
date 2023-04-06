package com.finalproject.recruit.jwt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.stereotype.Component;

@Component
@ConstructorBinding
@Getter
@RequiredArgsConstructor
@ToString
public class JwtProperties {
    @Value("${jwt.secret-key}")
    private String secretKey;
    @Value("${jwt.tokenPrefix}")
    private String tokenPrefix;
    @Value("${jwt.token.access-expired-time-ms}")
    private Long accessTokenExpiredTime;
    @Value("${jwt.token.refresh-expired-time-ms}")
    private Long refreshTokenExpiredTime;
}
