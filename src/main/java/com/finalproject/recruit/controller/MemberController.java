package com.finalproject.recruit.controller;

import com.finalproject.recruit.dto.member.AuthDTO;
import com.finalproject.recruit.dto.member.MemberReqDTO;
import com.finalproject.recruit.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
     **/
    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody MemberReqDTO.Login login) {
        return memberService.login(login);
    }


    /**
     * 로그아웃
     **/
    @PostMapping("/auth/logout")
    public ResponseEntity<?> logout(Authentication authentication) {

        String memberEmail = ((AuthDTO) authentication.getPrincipal()).getMemberEmail();
        String accessToken = authentication.getCredentials().toString();

        return memberService.logout(memberEmail, accessToken);
    }


    /**
     * 토큰 기한 연장
     **/
    @PostMapping("/auth/reissue")
    public ResponseEntity<?> reissue(Authentication authentication) {
        String memberEmail = ((AuthDTO) authentication.getPrincipal()).getMemberEmail();
        String accessToken = authentication.getCredentials().toString();
        return memberService.reissue(accessToken, memberEmail);
    }

    /**
     * 이메일 중복 체크
     **/
    @PostMapping("/auth/email_validation")
    public ResponseEntity<?> validateEmail(@RequestBody MemberReqDTO.EmailValidate emailValidate) {
        return memberService.existEmail(emailValidate.getMemberEmail());
    }

    /**
    * 비밀번호 재설정
    **/
    @PutMapping("/auth/resetPassword")
    public ResponseEntity<?> resetPassword(@RequestBody MemberReqDTO.ResetPassword resetPassword) {
        return memberService.resetPassword(resetPassword);
    }



    /**
     * 회원정보변경
     **/
    @PutMapping("/auth/updateMemberInfo")
    public ResponseEntity<?> updateMemberInfo(Authentication authentication, @RequestBody MemberReqDTO.Edit edit) {
        String memberEmail = ((AuthDTO) authentication.getPrincipal()).getMemberEmail();
        return memberService.updateMemberInfo(memberEmail, edit);
    }

    /**
     * 회원탈퇴
     **/
    @PutMapping("/auth/drop")
    public ResponseEntity<?> dropMember(Authentication authentication) {
        String memberEmail = ((AuthDTO) authentication.getPrincipal()).getMemberEmail();
        return memberService.dropMember(memberEmail);
    }

    @PostMapping("/auth/send_number")
    public ResponseEntity<?> sendAuthNumber(@RequestParam("memberEmail") String memberEmail) {
        System.out.println(memberEmail);
        return memberService.sendAuthNumber(memberEmail);
    }

    @PostMapping("/auth/check_number")
    public ResponseEntity<?> authNumber(@RequestBody MemberReqDTO.AuthMail authMail) {
        return memberService.numberAuth(authMail);
    }

}
