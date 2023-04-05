package com.finalproject.recruit.jwt;

import com.finalproject.recruit.dto.member.AuthDTO;
import com.finalproject.recruit.exception.authorization.AuthException;
import com.finalproject.recruit.exception.authorization.ErrorCode;
import com.finalproject.recruit.exception.member.MemberException;
import lombok.Builder;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Getter
public class JwtFilter extends OncePerRequestFilter {

    private final JwtManager jwtManager;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    @Builder
    private JwtFilter(JwtManager jwtManager){
        this.jwtManager = jwtManager;
    }

    public static JwtFilter of(JwtManager jwtManager){
        return new JwtFilter(jwtManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Header 추출
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);




        try{
            // 토큰 검증 & 검증유저 return
            AuthDTO member = jwtManager.getMemberInfoOf(header).orElseThrow(
                    () -> new AuthException(ErrorCode.INVALID_TOKEN));



            // 검증정보 Controller return
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    member,
                    jwtManager.extractToken(header),
                    member.getAuthorities()
            );
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        }catch(RuntimeException e){
            filterChain.doFilter(request, response);
            return;
        }
        filterChain.doFilter(request, response);
    }
}
