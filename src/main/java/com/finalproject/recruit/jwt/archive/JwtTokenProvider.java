package com.finalproject.recruit.jwt.archive;

import com.finalproject.recruit.dto.member.MemberReqDTO;
import com.finalproject.recruit.dto.member.MemberResDTO;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {

    private static final String AUTHORITIES_KEY  = "auth";

    private static final String BEARER_TYPE = "Bearer";

    private static final long ACCESS_TOKEN_EXPIRE_TIME = 30 * 60 * 1000L; //30분

    private static final long REFRESH_TOKEN_EXPIRE_TIME = 7 * 24 * 60 * 60 * 1000L; //7일

    private final String key = "test";
    /**
     * 유저 정보를 가지고 AccessToken, RereshToken을 생성하는 메소드
     */
    public MemberResDTO.TokenInfo generateToken(MemberReqDTO.Login login) {
        //권한 가져오기
//        String authorities = authentication.getAuthorities().stream()
//                .map(GrantedAuthority::getAuthority)
//                .collect(Collectors.joining());

        long now = (new Date()).getTime();

        //Access Token 생성
        Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
        String accessToken = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(login.getMemberEmail())
                .claim("email", login.getMemberEmail())
                .claim(AUTHORITIES_KEY, "MEMBER")
                .setExpiration(accessTokenExpiresIn)
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
        //Refresh Token 생성
        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now + REFRESH_TOKEN_EXPIRE_TIME))
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
        return MemberResDTO.TokenInfo.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .refreshTokenExpirationTime(REFRESH_TOKEN_EXPIRE_TIME)
                .build();
    }
    /**
     * JWT 토큰을 복호화하여 토큰에 들어있는 정보를 꺼내는 메소드
     */
    public Authentication getAuthentication(String accessToken) {
        //토큰 복호화
        Claims claims = parseClaims(accessToken);

        if (claims.get(AUTHORITIES_KEY) == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }
        //클레임에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(""))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        //UserDetails 객체를 만들어서 Authentication 리턴
        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(token);
            return true;
        }
        catch (SecurityException | MalformedJwtException err) {
            log.info("Invalid JWT Token", err);
        }
        catch (ExpiredJwtException err) {
            log.info("Expired JWT Token", err);
        }
        catch (UnsupportedJwtException err) {
            log.info("Unsupported JWT Token", err);
        }
        catch (IllegalArgumentException err) {
            log.info("JWT claims string is empty", err);
        }
        return false;
    }
    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parser().setSigningKey(key).parseClaimsJws(accessToken).getBody();
        }
        catch (ExpiredJwtException err) {
            return err.getClaims();
        }
    }
    public Long getExpiration(String accessToken) {
        //accessToken 남은 유효시간
        Date expiration = Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(accessToken)
                .getBody()
                .getExpiration();
        //현재시간
        Long now = new Date().getTime();
        return (expiration.getTime() - now);
    }
}
