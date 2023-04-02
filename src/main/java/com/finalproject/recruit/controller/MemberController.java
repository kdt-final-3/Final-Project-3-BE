package com.finalproject.recruit.controller;

import com.finalproject.recruit.dto.member.AuthDTO;
import com.finalproject.recruit.dto.member.MemberReqDTO;
import com.finalproject.recruit.service.MemberService;
import com.finalproject.recruit.service.archive.MemberServiceArch;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MemberController {

    // 신규 JWT 적용 service
    private final MemberService memberService;

    // 기존 Service
    private final MemberServiceArch memberServiceArch;

    /**
     * 회원가입
     */
    @PostMapping("/auth/signup")
    public ResponseEntity<?> signUp(@RequestBody MemberReqDTO.SignUp signUp) {
        return memberService.signUp(signUp);
    }

    /*===========================
        로그인
     ===========================*/
    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody MemberReqDTO.Login login) {
        return memberService.login(login);
    }

    /*===========================
        로그아웃
     ===========================*/
    @PostMapping("/auth/logout")
    public ResponseEntity<?> logout(Authentication authentication) {
        return memberService.logout(authentication);
    }

    /*===========================
        토큰기한 연장
     ===========================*/
    @PostMapping("/auth/reissue")
    public ResponseEntity<?> reissue(Authentication authentication) {
        AuthDTO member = (AuthDTO) authentication.getPrincipal();
        return memberService.reissue(authentication, member);
    }

    /**
     * 이메일 중복 체크
     */
    @GetMapping("/auth/email_validation")
    public ResponseEntity<?> validateEmail(@RequestBody MemberReqDTO.EmailValidate emailValidate) {
        return memberServiceArch.existEmail(emailValidate.getMemberEmail());
    }

    @PutMapping("/auth/resetPassword")
    public ResponseEntity<?> resetPassword(@RequestBody MemberReqDTO.ResetPassword resetPassword) {
        return memberServiceArch.resetPassword(resetPassword);
    }

    @PutMapping("/auth/updateMemberInfo")
    public ResponseEntity<?> updateMemberInfo(@RequestHeader("Authorization") String accessToken, @RequestBody MemberReqDTO.Edit edit) {
        return memberServiceArch.updateMemberInfo(accessToken, edit);
    }

    @PutMapping("/auth/drop")
    public ResponseEntity<?> dropMember(@RequestHeader("Authorization") String accessToken) {
        return memberServiceArch.dropMember(accessToken);
    }
    /*===========================
        Archive
     ===========================
     **
     * 로그인
     *
    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody MemberReqDTO.Login login) {
        return memberService.login(login);
    }
     **
     * 로그아웃
     *
    @PostMapping("/auth/logout")
    public ResponseEntity<?> logout(Authentication authentication) {
        return memberService.logout(authentication);
    }
     **
     * 토큰 기한 연장
     *
    @PostMapping("/auth/reissue")
    public ResponseEntity<?> reissue(@RequestHeader("Authorization") String accessToken, @AuthenticationPrincipal MemberReqDTO.Login login) {
        return memberService.reissue(accessToken, login);
    }

    */
}
