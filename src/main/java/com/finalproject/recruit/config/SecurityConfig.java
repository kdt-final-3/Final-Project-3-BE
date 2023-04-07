package com.finalproject.recruit.config;

import com.finalproject.recruit.exception.authorization.CustomAuthEntryPoint;
import com.finalproject.recruit.jwt.JwtFilter;
import com.finalproject.recruit.jwt.JwtManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
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

    private final JwtManager jwtManager;
    @Autowired
    private CustomAuthEntryPoint customAuthEntryPoint;

    /*===========================
        권한검사
    ===========================*/
    // 아래 URL은 권한검사X
    private static final String[] PUBLIC_URLS = {
            "/test", "/view/**", "/auth/login", "/auth/signup", "/auth/number"
    };
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() { //시큐리티 filter 제외, 그러나 OncePerRequestFilter는 시큐리티 필터가 아니라서 로직실행
        return (web) -> web.ignoring().mvcMatchers(PUBLIC_URLS);
    }


    /*===========================
        Security Chain
    ===========================*/
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http)throws Exception{

        return http
                .authorizeRequests()// 다음 리퀘스트에 대한 사용권한 체크
                .mvcMatchers(PUBLIC_URLS).permitAll() // 가입 및 인증 주소는 누구나 접근가능
                .and()
                .authorizeRequests()// 다음 리퀘스트에 대한 사용권한 체크
                .anyRequest().authenticated()// 그외 나머지 요청은 모두 인증된 회원만 접근 가능
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(customAuthEntryPoint)     // 토큰없이 접근하는 경우에 대해 Exception 발생
                .and()
                .cors()
                .configurationSource(corsConfigurationSource())
                .and()
                .csrf().disable()       // rest api이므로 csrf 보안이 필요없으므로 disable처리
                .httpBasic().disable()  // 기본설정 사용안함. 기본설정은 비인증시 로그인폼 화면으로 리다이렉트 된다.
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // jwt token으로 인증할것이므로 세션필요없으므로 생성안함.
                .and()
                .addFilterBefore(JwtFilter.of(jwtManager), UsernamePasswordAuthenticationFilter.class)
                .build();
        //인증을 처리하는 기본필터 UsernamePasswordAuthenticationFilter 대신 별도의 인증 로직을 가진 필터를 생성하고 사용하고 싶을 때 아래와 같이 필터를 등록하고 사용
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

    /*===========================
        ETC
     ===========================*/
    // 암호화에 필요한 PasswordEncoder Bean 등록
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
