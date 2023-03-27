package com.finalproject.recruit.service;

import com.finalproject.recruit.dto.Response;
import com.finalproject.recruit.dto.member.MemberReqDTO;
import com.finalproject.recruit.entity.Member;
import com.finalproject.recruit.jwt.JwtTokenProvider;
import com.finalproject.recruit.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepo;

    private final JwtTokenProvider provider;

    private final Response response;

    private final RedisTemplate redisTemplate;

    private AuthenticationManagerBuilder authenticationManagerBuilder;

    private final PasswordEncoder encoder;

    private static final String pattern = "^[A-Za-z[0-9]]{8,16}$"; // 영문, 숫자 8~16자리

    /**
     * 회원가입
     */
    public ResponseEntity<?> signUp(MemberReqDTO.SignUp signUp) {
        if (memberRepo.existsByMemberEmail(signUp.getMemberEmail())) {
            return response.fail("이미 가입된 이메일입니다.", HttpStatus.BAD_REQUEST);
        }

        Member member = Member.builder()
                .memberEmail(signUp.getMemberEmail())
                .password(encoder.encode(signUp.getPassword()))
                .memberPhone(signUp.getMemberPhone())
                .ceoName(signUp.getCeoName())
                .companyName(signUp.getCompanyName())
                .companyNum(signUp.getCompanyNum())
                .memberDelete(false)
                .build();
        memberRepo.save(member);
        return response.success("회원가입에 성공하였습니다.");
    }
}