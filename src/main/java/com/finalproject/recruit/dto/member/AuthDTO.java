package com.finalproject.recruit.dto.member;

import io.jsonwebtoken.Claims;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthDTO {

    private String memberEmail;
    private String role;
    public AuthDTO(Claims claims) {
        this.memberEmail = claims.get("memberEmail", String.class);
        this.role = claims.get("role", String.class);
    }
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_MEMBER"));
    }
}
