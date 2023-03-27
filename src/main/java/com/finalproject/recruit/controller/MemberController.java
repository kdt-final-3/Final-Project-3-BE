package com.finalproject.recruit.controller;

import com.finalproject.recruit.dto.member.MemberReqDTO;
import com.finalproject.recruit.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 비밀번호 재설정
     */
    @PutMapping("/auth/reset_password")
    public ResponseEntity<?> resetPassword(@RequestBody MemberReqDTO.ResetPassword resetPassword) {
        return memberService.resetPassword(resetPassword);
    }

    /**
     * 회원 정보 변경
     */
    @PutMapping("/edit")
    public ResponseEntity<?> editInfo(@RequestHeader("Authorization") String accessToken, @RequestBody MemberReqDTO.Edit edit) {
        return memberService.editInfo(accessToken, edit);
    }
}
