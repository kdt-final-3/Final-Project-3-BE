package com.finalproject.recruit.controller;

import com.finalproject.recruit.dto.member.MemberReqDTO;
import com.finalproject.recruit.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
public class MemberController {


    private final MemberService memberService;

    /**
     * 회원가입
     */
    @PostMapping("/auth/signup")
    public ResponseEntity<?> signUp(@RequestBody MemberReqDTO.SignUp signUp) {
        return memberService.signUp(signUp);
    }

    /**
     * 로그인
     */
    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody MemberReqDTO.Login login) {
        return memberService.login(login);
    }

    /**
     * 로그아웃
     */
    @PostMapping("/auth/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String accessToken, @AuthenticationPrincipal MemberReqDTO.Login login) {
        return memberService.logout(accessToken, login);
    }

    /**
     * 토큰 기한 연장
     */
    @PostMapping("/auth/reissue")
    public ResponseEntity<?> reissue(@RequestHeader("Authorization") String accessToken) {
        return memberService.reissue(accessToken);
    }

    /**
     * 이메일 중복 체크
     */
    @GetMapping("/auth/email_validation")
    public ResponseEntity<?> validateEmail(@RequestBody MemberReqDTO.EmailValidate emailValidate) {
        return memberService.existEmail(emailValidate.getMemberEmail());
    }


}
