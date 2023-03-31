package com.finalproject.recruit.controller;

import com.finalproject.recruit.dto.member.MemberReqDTO;
import com.finalproject.recruit.entity.Member;
import com.finalproject.recruit.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
        System.out.println(login.getMemberEmail());
        System.out.println(login.getPassword());
        return memberService.login(login);
    }

    /**
     * 로그아웃
     */
    @PostMapping("/auth/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String accessToken) {
        return memberService.logout(accessToken);
    }

    /**
     * 토큰 기한 연장
     */
    @PostMapping("/auth/reissue")
    public ResponseEntity<?> reissue(@RequestHeader("Authorization") String accessToken, @AuthenticationPrincipal MemberReqDTO.Login login) {
        return memberService.reissue(accessToken, login);
    }

    /**
     * 이메일 중복 체크
     */
    @GetMapping("/auth/email_validation")
    public ResponseEntity<?> validateEmail(@RequestBody MemberReqDTO.EmailValidate emailValidate) {
        return memberService.existEmail(emailValidate.getMemberEmail());
    }

    @PutMapping("/auth/resetPassword")
    public ResponseEntity<?> resetPassword(@RequestBody MemberReqDTO.ResetPassword resetPassword) {
        return memberService.resetPassword(resetPassword);
    }

    @PutMapping("/auth/updateMemberInfo")
    public ResponseEntity<?> updateMemberInfo(@RequestHeader("Authorization") String accessToken, @RequestBody MemberReqDTO.Edit edit) {
        return memberService.updateMemberInfo(accessToken, edit);
    }

    @PutMapping("/auth/drop")
    public ResponseEntity<?> dropMember(@RequestHeader("Authorization") String accessToken) {
        return memberService.dropMember(accessToken);
    }

}
