package com.finalproject.recruit.jwt;

import com.finalproject.recruit.dto.member.AuthDTO;
import com.finalproject.recruit.entity.Member;
import com.finalproject.recruit.service.TokenService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
            return Optional.empty(); }
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
            if(
                    (extractClaims(token) != null) &&
                    (tokenService.isMemberValid(extractMember(token)))
            ){
                return true;
            }
            return false;
        } catch (SecurityException | MalformedJwtException err) {
            log.info("Invalid JWT Token", err);
        } catch (ExpiredJwtException err) {
            log.info("Expired JWT Token", err);
        } catch (UnsupportedJwtException err) {
            log.info("Unsupported JWT Token", err);
        } catch (IllegalArgumentException err) {
            log.info("JWT claims string is empty", err);
        }
        return false;
    }

    // 토큰 발급자 확인
    public String extractMember(String token){
        return extractClaims(token).get("memberEmail", String.class);
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
