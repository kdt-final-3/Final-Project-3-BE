package com.finalproject.recruit.service;

import com.finalproject.recruit.dto.Response;
import com.finalproject.recruit.dto.member.MemberReqDTO;
import com.finalproject.recruit.entity.Member;
import com.finalproject.recruit.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepo;

    private final Response response;

    private final RedisTemplate redisTemplate;

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

    /**
     * 로그인

    public ResponseEntity<?> login(MemberReqDTO.Login login) {

        Member member = memberRepo.findByMemberEmail(login.getMemberEmail()).orElse(null);
        if (member == null) {
            return response.fail("해당되는 유저가 존재하지 않습니다.");
        }

        UsernamePasswordAuthenticationToken authenticationToken = login.toAuthentication();

        //Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        MemberResDTO.TokenInfo tokenInfo = provider.generateToken(login);

        redisTemplate.opsForValue()
                .set("RT : " + login.getMemberEmail(), tokenInfo.getRefreshToken(), tokenInfo.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);
        return response.success(tokenInfo, "로그인에 성공하셨습니다.", HttpStatus.OK);
    }
     */

    /**
     * 로그아웃

    public ResponseEntity<?> logout(String accessToken) {
        System.out.println(accessToken);

        Authentication authentication = provider.getAuthentication(accessToken);
        System.out.println(authentication.toString());

        if (redisTemplate.opsForValue().get("RT : " + authentication.getName()) != null) {
            redisTemplate.delete("RT : " + authentication.getName());
        }

        Long expireTime = provider.getExpiration(accessToken);

        redisTemplate.opsForValue()
                .set(accessToken, "logout", expireTime, TimeUnit.MILLISECONDS);

        return response.success("로그아웃 되셨습니다.");
    }
     */

    /**
     * 토큰 기한 연장
    public ResponseEntity<?> reissue(String accessToken, MemberReqDTO.Login login) {
        if (!provider.validateToken(accessToken)) {
            return response.fail("RefreshToken의 정보가 유효하지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        Authentication authentication = provider.getAuthentication(accessToken);

        String refreshToken = (String)redisTemplate.opsForValue().get("RT : " + authentication.getName());

        if (ObjectUtils.isEmpty(refreshToken)) {
            return response.fail("잘못된 요청입니다.", HttpStatus.BAD_REQUEST);
        }

        MemberResDTO.TokenInfo tokenInfo = provider.generateToken(login);

        redisTemplate.opsForValue()
                .set("RT : " + authentication.getName(), tokenInfo.getRefreshToken(), tokenInfo.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);

        return response.success(tokenInfo, "갱신하였습니다.", HttpStatus.OK);
    }

    public ResponseEntity<?> existEmail(String email) {
        return memberRepo.existsByMemberEmail(email)?
                response.fail("이미 가입된 이메일입니다."):
        response.success("가입 가능한 이메일입니다.");
    }
     */

    @Transactional
    public ResponseEntity<?> resetPassword(MemberReqDTO.ResetPassword password) {
        System.out.println(password.getNewPassword());
        if (!password.getNewPassword().equals(password.getPasswordCheck())) {
            return response.fail("일치하지 않습니다");
        }
        Member member = memberRepo.findByMemberEmail(password.getMemberEmail()).orElse(null);

        try {
            member.resetPassword(encoder.encode(password.getNewPassword()));
            System.out.println(member.getPassword());
        }
        catch (Exception e) {
            e.printStackTrace();
            return response.fail("실패");
        }
        return response.success();

    }

    /*
    @Transactional
    public ResponseEntity<?> updateMemberInfo(String accessToken, MemberReqDTO.Edit edit) {
        Member member = memberRepo.findByMemberEmail(provider.getAuthentication(accessToken).getName()).orElse(null);
        try {
            edit.setPassword(encoder.encode(edit.getPassword()));
            member.updateMemberInfo(edit);
        }
        catch (Exception e) {
            e.printStackTrace();
            return response.fail("실패");
        }
        return response.success();
    }
    @Transactional
    public ResponseEntity<?> dropMember(String accessToken) {
        Member member = memberRepo.findByMemberEmail(provider.getAuthentication(accessToken).getName()).orElse(null);
        try {
            member.dropMember();
        }
        catch (Exception e) {
            e.printStackTrace();
            return response.fail("실패");
        }
        return response.success();
    }
    */
}
