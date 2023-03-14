package com.finalproject.recruit.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {

    /*===========================
        권한검사
    ===========================*/
    // 아래 URL은 권한검사X
    private static final String[] PUBLIC_URLS = {
            "/signup", "/login", "/logout", "/userIdValidation"
    };

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() { //시큐리티 filter 제외, 그러나 OncePerRequestFilter는 시큐리티 필터가 아니라서 로직실행
        return (web) -> web.ignoring().mvcMatchers(PUBLIC_URLS);
    }


    /*===========================
        CORS
     ===========================*/
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // 모든 Origin에서의 요청을 허용
        configuration.setAllowedOriginPatterns(Collections.singletonList("*"));

        // 해당 Http Methods를 사용하는 요청을 허용
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));

        // 해당 헤더를 사용하는 요청을 허용
        configuration.setAllowedHeaders(List.of("authorization", "content-type", "x-auth-token"));

        // 헤더에 CSRF 토큰이 있는 요청에 대해 모든 응답 헤더를 노출
        configuration.setExposedHeaders(Collections.singletonList("x-auth-token"));

        // 사용자 자격 증명(쿠키, 인증키) 사용을 허용할 것
        configuration.setAllowCredentials(true);

        // CORS 설정값(configuration)을 주입할 Source 생성
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        // 모든 URL에 대해 위의 설정을 사용해 CORS 처리를 할 것
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
