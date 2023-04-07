package com.finalproject.recruit.jwt;

import com.finalproject.recruit.dto.member.AuthDTO;
import com.finalproject.recruit.entity.Member;
import com.finalproject.recruit.exception.authorization.AuthException;
import com.finalproject.recruit.exception.authorization.ErrorCode;
import com.finalproject.recruit.exception.member.MemberException;
import com.finalproject.recruit.service.TokenService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtManager {
    private final JwtProperties jwtProperties;
    private final TokenService tokenService;
    private final RedisTemplate redisTemplate;

    /*===========================
        토큰 생성
    ===========================*/
    // Access 토큰
    public String generateAccessToken(Member member, Long expiredTime){
        Date now = new Date();
        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuedAt(now)
                .claim("memberEmail", member.getMemberEmail())
                .claim("role", "ROLE_MEMBER")
                .setExpiration(new Date(now.getTime() + expiredTime))
                .signWith(SignatureAlgorithm.HS256, getKey(jwtProperties.getSecretKey()))
                .compact();
    }

    // Refresh 토큰
    public String generateRefreshToken(Member member, Long expiredTime){
        Date now = new Date();
        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuedAt(now)
                .claim("memberEmail", member.getMemberEmail())
                .claim("role", "ROLE_MEMBER")
                .setExpiration(new Date(now.getTime() + expiredTime))
                .signWith(SignatureAlgorithm.HS256, getKey(jwtProperties.getSecretKey()))
                .compact();
    }

    /*=============================================
        AuthDTO : 헤더검증 -> 토큰검증 -> 검증값 반환
    ===============================================*/
    public Optional<AuthDTO> getMemberInfoOf(String header){
        // Header 검증
        if (!headerValidation(header)) {
            throw new AuthException(ErrorCode.INVALID_HEADER);
        }
        try {
            // 토큰 추출
            String token = extractToken(header);

            // Token 검증
            if(!isValid(token)){
                return Optional.empty();
            }

            // Claims 추출
            Claims claims = extractClaims(token);

            // Auth정보가 담겨진 DTO 반환
            return Optional.of(new AuthDTO(claims));

        }catch(Exception e){
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /*===========================
        헤더 검증
    ===========================*/
    // Header 유효성 검증
    private boolean headerValidation(String header){
        return header != null && header.startsWith(jwtProperties.getTokenPrefix());
    }

    // Header 에서 Token 추출
    public String extractToken(String header){
        return header.split(" ")[1].trim();
    }

    /*===========================
        토큰 검증 & 추출
    ===========================*/
    // 토큰값에서 claim 추출
    public Claims extractClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getKey(jwtProperties.getSecretKey()))
                .build().parseClaimsJws(token).getBody();
    }

    // 토큰 유효성 확인
    public boolean isValid(String token){
        try{
            if (redisTemplate.opsForValue().get(token) != null) {
                throw new AuthException(ErrorCode.EXPIRED_REDIS_TOKEN);
            }
            return (extractClaims(token) != null) &&
                    (tokenService.isMemberValid(extractMember(token)));
        } catch (SecurityException | MalformedJwtException err) {
            log.error("Invalid JWT Token", err);
            throw new AuthException(ErrorCode.INVALID_TOKEN);
        } catch (ExpiredJwtException err) {
            log.error("Expired JWT Token", err);
            throw new AuthException(ErrorCode.EXPIRED_TOKEN);
        } catch (UnsupportedJwtException err) {
            log.error("Unsupported JWT Token", err);
            throw new AuthException(ErrorCode.INVALID_TOKEN);
        } catch (IllegalArgumentException err) {
            log.error("JWT claims string is empty", err);
            throw new AuthException(ErrorCode.EMPTY_CLAMIS);
        }
    }

    // 토큰정보 추출
    public String extractMember(String token){
        return extractClaims(token).get("memberEmail", String.class);
    }

    // 토큰만료값 추출
    public long expiredTime(String token){
        return extractClaims(token).getExpiration().getTime();
    }

    /*===========================
        ETC
    ===========================*/
    private static Key getKey(String key){
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Long getExpiredTime(String token){
        return extractClaims(token).getExpiration().getTime();
    }
}
