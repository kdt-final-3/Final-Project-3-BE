package com.finalproject.recruit.service;

import com.finalproject.recruit.dto.Response;
import com.finalproject.recruit.dto.member.MemberReqDTO;
import com.finalproject.recruit.dto.member.MemberResDTO;
import com.finalproject.recruit.entity.Member;
import com.finalproject.recruit.exception.member.ErrorCode;
import com.finalproject.recruit.exception.member.MemberException;
import com.finalproject.recruit.jwt.JwtManager;
import com.finalproject.recruit.jwt.JwtProperties;
import com.finalproject.recruit.repository.MailRepository;
import com.finalproject.recruit.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepo;

    private final Response response;

    private final RedisTemplate redisTemplate;

    private final JwtProperties properties;
    private final PasswordEncoder encoder;

    private final JwtManager manager;

    private final MailRepository mailRepository;

    private final JavaMailSender mailSender;

    private static final Long TIME_OUT = 3 * 60 * 1800L;

    /*===========================
        회원가입
     ===========================*/
    public ResponseEntity<?> signUp(MemberReqDTO.SignUp signUp) {
        try{
            // 이메일 중복검증
            if (memberRepo.existsByMemberEmail(signUp.getMemberEmail())) {
                return response.fail(
                        String.format("%s : %s", ErrorCode.ALREADY_EXIST.getMessage(), signUp.getMemberEmail()),
                        ErrorCode.ALREADY_EXIST.getStatus());
            }

            // 등록할 회원객체 생성
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
            return response.success("Successfully SignUp");

        }catch(MemberException e){
            e.printStackTrace();
            throw new MemberException(ErrorCode.SIGNUP_FAILED);
        }
    }

    /*===========================
        로그인
     ===========================*/
    public ResponseEntity<?> login(MemberReqDTO.Login login) {
        try{
            // 이메일 검증
            Member member = memberRepo.findByMemberEmail(login.getMemberEmail())
                    .orElseThrow(() -> new MemberException(ErrorCode.MEMBER_NOT_FOUND));

            // 비밀번호 검증
            if (!checkPassword(login.getPassword(), member.getPassword())) {
                return response.fail(
                        ErrorCode.INCORRECT_PASSWORD.getMessage(),
                        ErrorCode.INCORRECT_PASSWORD.getStatus());
            }

            // 로그인 객체생성
            MemberResDTO.TokenInfo loginInfo = new MemberResDTO.TokenInfo(
                    manager.generateAccessToken(member, properties.getAccessTokenExpiredTime()),
                    manager.generateRefreshToken(member, properties.getRefreshTokenExpiredTime()),
                    properties.getRefreshTokenExpiredTime()
            );

            // Redis 저장
            registToRedis(loginInfo, member.getMemberEmail());

            // 결과전달
            return response.success(
                    loginInfo,
                    "Successfully Login");

        }catch(MemberException e){
            e.printStackTrace();
            throw new MemberException(ErrorCode.LOGIN_FAILED);
        }
    }

    /*===========================
        로그아웃
     ===========================*/
    public ResponseEntity<?> logout(String memberEmail, String accessToken) {
        try{
            Long expireTime = manager.getExpiredTime(accessToken);
            deleteFromRedis(memberEmail, accessToken, expireTime);

            return response.success("Successfully Logout");

        }catch (MemberException e){
            e.printStackTrace();
            throw new MemberException(ErrorCode.LOGOUT_FAILED);
        }
    }

    /*===========================
        토큰 유효기간 연장
     ===========================*/
    public ResponseEntity<?> reissue(String accessToken, String memberEmail) {
        try {
            // 토큰검증
            if (!manager.isValid(accessToken)) {
                return response.fail(
                        ErrorCode.INVALID_TOKEN.getMessage(),
                        ErrorCode.INVALID_TOKEN.getStatus());
            }
            // 회원객체 검증
            Member member = memberRepo.findByMemberEmail(memberEmail).orElseThrow(
                    () -> new MemberException(ErrorCode.MEMBER_NOT_FOUND));

            // Redis로 부터 refreshToken 추출
            String refreshToken = loadAuthFromRedis(memberEmail);

            // 토큰 신규발급
            MemberResDTO.TokenInfo memberInfo = new MemberResDTO.TokenInfo(
                    manager.generateAccessToken(member, properties.getAccessTokenExpiredTime()),
                    manager.generateRefreshToken(member, properties.getRefreshTokenExpiredTime()),
                    properties.getRefreshTokenExpiredTime()
            );

            // 토큰정보 갱신
            registToRedis(memberInfo, memberEmail);

            return response.success(
                    memberInfo,
                    "Successfully Extend Token ExpiredTime");
        } catch (MemberException e) {
            e.printStackTrace();
            throw new MemberException(ErrorCode.EXTEND_TOKEN_TIME_FAILED);
        }
    }

    /*===========================
        이메일 중복확인
     ===========================*/
    public ResponseEntity<?> existEmail(String email) {
        return memberRepo.existsByMemberEmail(email)?
                response.fail(
                        ErrorCode.ALREADY_EXIST.getMessage(),
                        ErrorCode.ALREADY_EXIST.getStatus()):
                response.success("Available Email For Signup");
    }

    /*===========================
        비밀번호 관련
     ===========================*/
    // 비밀번호 재설정
    @Transactional
    public ResponseEntity<?> resetPassword(MemberReqDTO.ResetPassword password) {
        try{
            // 신규 비밀번호 일치확인
            if (!password.getNewPassword().equals(password.getPasswordCheck())) {
                return response.fail(
                        ErrorCode.MISMATCH_PASSWORD.getMessage(),
                        ErrorCode.MISMATCH_PASSWORD.getStatus());
            }

            // 멤버정보 확인
            Member member = memberRepo.findByMemberEmail(password.getMemberEmail()).orElseThrow(
                    () -> new MemberException(ErrorCode.MEMBER_NOT_FOUND));

            // 비밀번호 변경 및 검증
            resetPasswordValidation(member, password.getNewPassword(), password.getMemberEmail());

            return response.success("Successfully Update Password");
        }catch (Exception e) {
            e.printStackTrace();
            return response.fail(
                    ErrorCode.UNABLE_TO_PROCESS_REQUEST.getMessage(),
                    ErrorCode.UNABLE_TO_PROCESS_REQUEST.getStatus());
        }

    }

    // 비밀번호 변경 & 검증
    @Transactional
    public void resetPasswordValidation(Member member, String password, String email){
        try{
            member.resetPassword(encoder.encode(password));
            Member updateMember = memberRepo.save(member);

            // 비밀번호 업데이트 검증
            if(!checkPassword(member.getPassword(), updateMember.getPassword())){
                throw new MemberException(ErrorCode.PASSWORD_UPDATE_FAILED);
            }

        }catch (MemberException e){
            e.printStackTrace();
            throw new MemberException(ErrorCode.PASSWORD_UPDATE_FAILED);
        }
    }

    // 비밀번호 확인
    private boolean checkPassword(String input, String origin) {
        return encoder.matches(input, origin);
    }

    /*===========================
        멤버정보 변경
     ===========================*/
    @Transactional
    public ResponseEntity<?> updateMemberInfo(String memberEmail, MemberReqDTO.Edit edit) {
        try{
            Member member = memberRepo.findByMemberEmail(memberEmail).orElseThrow(
                    () -> new MemberException(ErrorCode.MEMBER_NOT_FOUND));

            // 비밀번호 변경 희망시
            if (edit.getPassword() != null) {
                member.resetPassword(encoder.encode(edit.getPassword()));
            }

            // 회원정보 변경
            member.updateMemberInfo(edit);
            Member updateMember = memberRepo.save(member);

            // 변경정보 검증
            if(!updateMember.equals(member)){
                return response.fail(
                        ErrorCode.INFO_UPDATE_FAILED.getMessage(),
                        ErrorCode.INFO_UPDATE_FAILED.getStatus());
            }
            return response.success("Successfully Update MemberInfo");
        }catch (MemberException e){
            e.printStackTrace();
            throw new MemberException(ErrorCode.INFO_UPDATE_FAILED);
        }
    }

    /*===========================
        멤버삭제 (soft)
    ===========================*/
    @Transactional
    public ResponseEntity<?> dropMember(String memberEmail) {
        try{
            Member member = memberRepo.findByMemberEmail(memberEmail).orElseThrow(
                    () -> new MemberException(ErrorCode.MEMBER_NOT_FOUND));

            // 멤버삭제
            member.dropMember();
            Member deleteMember = memberRepo.save(member);

            if(!deleteMember.isMemberDelete()){
                return response.fail(
                        ErrorCode.MEMBER_DELETE_FAILED.getMessage(),
                        ErrorCode.MEMBER_DELETE_FAILED.getStatus());
            }
            return response.success("Successfully Delete MemberInfo");
        } catch (MemberException e) {
            e.printStackTrace();
            throw new MemberException(ErrorCode.MEMBER_DELETE_FAILED);
        }
    }

    /*===========================
        인증번호 전송
    ===========================*/
    public ResponseEntity<?> sendAuthNumber(String memberEmail) {
        try {
            // 인증번호 생성
            String resultNum = generateNumber();
            if(resultNum == null){
                return response.fail(
                        ErrorCode.NUMBER_GENERATE_FAILED.getMessage(),
                        ErrorCode.NUMBER_GENERATE_FAILED.getStatus());
            }

            // 메일전송
            mailSend(memberEmail, resultNum);

            // Redis 등록
            registToRedis(memberEmail, resultNum);


            return response.success("Successfully Send Authorization Info");
        }
        catch (MemberException e) {
            e.printStackTrace();
            throw new MemberException(ErrorCode.UNABLE_TO_PROCESS_REQUEST);
        }
    }

    // 난수생성
    public String generateNumber(){
        Random rand = new Random();
        int number = rand.nextInt(900000) + 100000;
        return String.valueOf(number);
    }

    // 메세지 전송
    public void mailSend(String email, String number){
        // 전송객체 생성
        SimpleMailMessage authNum = new SimpleMailMessage();
        authNum.setSubject(email + " 인증 번호입니다.");
        authNum.setTo(email);
        authNum.setText(number);

        try{
            mailSender.send(authNum);
        }catch(MemberException e){
            e.printStackTrace();
            throw new MemberException(ErrorCode.UNABLE_TO_SEND_MESSAGE);
        }
    }

    /*===========================
        번호인증
    ===========================*/
    public ResponseEntity<?> numberAuth(MemberReqDTO.AuthMail authMail) {
        try{
            String memberEmail = authMail.getMemberEmail();
            String authNum = authMail.getAuthNumber();

            // Redis 정보추출
            String extractNum = loadNumFromRedis(memberEmail);

            // 인증번호 검증
            if (!extractNum.equals(authNum)) {
                return response.fail(
                        ErrorCode.INCORRECT_AUTH_NUM.getMessage(),
                        ErrorCode.INCORRECT_AUTH_NUM.getStatus());
            }

            // 검증완료
            deleteFromRedis(memberEmail);
            return response.success();
        }catch(MemberException e){
            e.printStackTrace();
            throw new MemberException(ErrorCode.AUTH_NUM_VALIDATION_FAILED);
        }
    }

    /*===========================
        회원정보 추출
    ===========================*/
    public Member loadMemberByMemberEmail(String memberEmail){
        return memberRepo.findByMemberEmail(memberEmail).orElseThrow(
                () -> new MemberException(ErrorCode.MEMBER_NOT_FOUND)
        );
    }
    /*===========================
        Redis
     ===========================*/
    // Token 정보등록
    public void registToRedis(MemberResDTO.TokenInfo input, String email){
        try{
            // Redis 등록
            redisTemplate.opsForValue()
                    .set("RT : " + email,
                            input.getRefreshToken(),
                            input.getRefreshTokenExpirationTime(),
                            TimeUnit.MILLISECONDS
                    );

            // 등록값 검증
            if(redisTemplate.opsForValue().get("RT : " + email) == null){
                throw new MemberException(ErrorCode.REDIS_VERIFIED_FALID);
            }

        }catch(MemberException e){
            e.printStackTrace();
            throw new MemberException(ErrorCode.REDIS_REGIST_FAILED);
        }
    }

    // 인증번호 정보등록
    public void registToRedis(String email, String resultNum){
        try{
            // Redis 등록
            redisTemplate.opsForValue()
                    .set("Auth : " + email,
                            resultNum,
                            TIME_OUT,
                            TimeUnit.MILLISECONDS
                    );

            // 등록값 검증
            if(redisTemplate.opsForValue().get("Auth : " + email) == null){
                throw new MemberException(ErrorCode.REDIS_VERIFIED_FALID);
            }
        }catch(MemberException e){
            e.printStackTrace();
            throw new MemberException(ErrorCode.REDIS_REGIST_FAILED);
        }
    }

    // Token 정보추출
    public String loadAuthFromRedis(String email){
        try{
            String data = (String) redisTemplate.opsForValue().get("RT : " + email);
            if(data == null || data.isEmpty()){
                throw new MemberException(ErrorCode.REDIS_AUTH_NOT_FOUND);
            }else{
                return data;
            }
        }catch(MemberException e){
            e.printStackTrace();
            throw new MemberException(ErrorCode.REDIS_AUTH_LOAD_FAILED);
        }
    }
    // 인증번호 정보추출
    public String loadNumFromRedis(String email){
        try{
            String data = (String) redisTemplate.opsForValue().get("Auth : " + email);
            if(data == null || data.isEmpty()){
                throw new MemberException(ErrorCode.REDIS_NUM_EXPIRED);
            }else{
                return data;
            }
        }catch(MemberException e){
            e.printStackTrace();
            throw new MemberException(ErrorCode.REDIS_NUM_LOAD_FAILED);
        }
    }

    // Token 정보삭제
    public void deleteFromRedis(String email, String token, long expiredTime){
        try{
            // Redis 저장데이터 검증
            if(redisTemplate.opsForValue().get("RT : " + email) == null){
                throw new MemberException(ErrorCode.REDIS_SAVED_INFO_NOT_FOUND);
            }

            // Redis 저장데이터 삭제
            redisTemplate.delete("RT : " + email);

            // 만료정보 Redis 등록
            redisTemplate.opsForValue()
                    .set(token, "logout", expiredTime, TimeUnit.MILLISECONDS);

        }catch(MemberException e){
            e.printStackTrace();
            throw new MemberException(ErrorCode.REDIS_DELETE_FAILED);
        }
    }
    // 인증 정보삭제
    public void deleteFromRedis(String email){
        try{
            // Redis 저장데이터 검증
            if(redisTemplate.opsForValue().get("Auth : " + email) == null){
                throw new MemberException(ErrorCode.REDIS_SAVED_INFO_NOT_FOUND);
            }

            // Redis 저장데이터 삭제
            redisTemplate.delete("Auth : " + email);

        }catch(MemberException e){
            e.printStackTrace();
            throw new MemberException(ErrorCode.REDIS_DELETE_FAILED);
        }
    }
}
