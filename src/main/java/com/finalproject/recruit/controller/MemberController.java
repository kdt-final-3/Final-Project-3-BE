package com.finalproject.recruit.controller;

import com.finalproject.recruit.dto.member.MemberReqDTO;
import com.finalproject.recruit.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String accessToken) {
        return memberService.logout(accessToken);
    }
}
