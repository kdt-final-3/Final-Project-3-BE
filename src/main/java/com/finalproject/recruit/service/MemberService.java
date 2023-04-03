package com.finalproject.recruit.service;

import com.finalproject.recruit.dto.member.AuthDTO;
import com.finalproject.recruit.dto.Response;
import com.finalproject.recruit.dto.member.MemberReqDTO;
import com.finalproject.recruit.dto.member.MemberResDTO;
import com.finalproject.recruit.entity.Member;
import com.finalproject.recruit.jwt.JwtManager;
import com.finalproject.recruit.jwt.JwtProperties;
import com.finalproject.recruit.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepo;

    private final Response response;

    private final RedisTemplate redisTemplate;

    private final PasswordEncoder encoder;

    private static final String pattern = "^[A-Za-z[0-9]]{8,16}$"; // 영문, 숫자 8~16자리

     // Security 관련
    private final JwtManager jwtManager;
    private final JwtProperties jwtProperties;

    /*===========================
        회원가입
     ===========================*/
    public ResponseEntity<?> signUp(MemberReqDTO.SignUp signUp) {
        // 아이디 중복확인
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

    /*===========================
        로그인
     ===========================*/
    public ResponseEntity<?> login(MemberReqDTO.Login login) {
        // 아이디 존재여부 확인
        Member member = memberRepo.findByMemberEmail(login.getMemberEmail()).orElseThrow(
                () -> new RuntimeException());

        // 비밀번호 일치여부 확인
        if(!checkPassword(login.getPassword(), member.getPassword())){
            return response.fail("비밀번호가 일치하지 않습니다", HttpStatus.BAD_REQUEST);
        }

        // 토큰발급
        MemberResDTO.TokenInfo tokenInfo = new MemberResDTO.TokenInfo(
                jwtManager.generateAccessToken(member, jwtProperties.getAccessTokenExpiredTime()),
                jwtManager.generateRefreshToken(member, jwtProperties.getRefreshTokenExpiredTime()),
                jwtProperties.getRefreshTokenExpiredTime()
        );

        /* Redis 저장
        redisTemplate.opsForValue()
                .set("RT : " + login.getMemberEmail(), tokenInfo.getRefreshToken(), tokenInfo.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);
         */

        // 결과값 Return
        return response.success(tokenInfo, "로그인에 성공하셨습니다.", HttpStatus.OK);
    }

    /*===========================
        로그아웃
     ===========================*/
    public ResponseEntity<?> logout(Authentication authentication) {
        if(authentication == null){
            return response.fail("인증되지 않은 사용자입니다.", HttpStatus.UNAUTHORIZED);
        }

        String token = (String) authentication.getCredentials();
        System.out.println("input token : " + token);

        /* Redis 저장 Token 제거
        if (redisTemplate.opsForValue().get("RT : " + authentication.getName()) != null) {
            redisTemplate.delete("RT : " + authentication.getName());
        }
         */

        Long expireTime = jwtManager.getExpiredTime(token);
        System.out.println("token expired time : " + expireTime);

        /* Redis 블랙리스트에 토큰추가
        redisTemplate.opsForValue()
                .set(accessToken, "logout", expireTime, TimeUnit.MILLISECONDS);
         */

        return response.success("로그아웃 되셨습니다.");
    }



    /*===========================
        토큰 유효시간 연장
     ===========================*/
    public ResponseEntity<?> reissue(Authentication authentication, AuthDTO member) {

        // authentication 에서 token 추출
        String token = (String) authentication.getCredentials();

        // token 유효성 확인
        if(!jwtManager.isValid(token)){
            return response.fail("유효하지 않은 정보입니다", HttpStatus.UNAUTHORIZED);
        }

        /* Redis 에서 Refresh 토큰추출
        String refreshToken = (String)redisTemplate.opsForValue().get("RT : " + authentication.getName());

        if (ObjectUtils.isEmpty(refreshToken)) {
            return response.fail("잘못된 요청입니다.", HttpStatus.BAD_REQUEST);
        }
         */

        // 새로운 토큰생성
        //String newGenerateToken = jwtManager.generateAccessToken();

        /* 신규토큰 정보 Redis 업데이트
        redisTemplate.opsForValue()
                .set("RT : " + authentication.getName(), tokenInfo.getRefreshToken(), tokenInfo.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);
         */

        //return response.success("갱신완료", HttpStatus.OK);
        return null;
    }


    /*===========================
        비밀번호 초기화
     ===========================
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


    /*===========================
        정보 업데이트
     ===========================
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

    /*===========================
        회원탈퇴
     ===========================
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

    /*===========================
        ETC
     ===========================*/
    // 가입여부 확인
    public ResponseEntity<?> existEmail(String email) {
        return memberRepo.existsByMemberEmail(email)?
                response.fail("이미 가입된 이메일입니다."):
                response.success("가입 가능한 이메일입니다.");
    }

    // 비밀번호 일치여부 확인
    private boolean checkPassword(String inputPassword, String originPassword) {
        return encoder.matches(inputPassword, originPassword);
    }

}
