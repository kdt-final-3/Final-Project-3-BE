package com.finalproject.recruit.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.finalproject.recruit.dto.Response;
import com.finalproject.recruit.dto.recruit.RecruitReq;
import com.finalproject.recruit.dto.recruit.RecruitRes;
import com.finalproject.recruit.entity.Member;
import com.finalproject.recruit.entity.Recruit;
import com.finalproject.recruit.exception.keep.KeepException;
import com.finalproject.recruit.exception.member.MemberException;
import com.finalproject.recruit.exception.recruit.ErrorCode;
import com.finalproject.recruit.exception.recruit.RecruitException;
import com.finalproject.recruit.parameter.Procedure;
import com.finalproject.recruit.repository.RecruitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class RecruitService {
    private static final String SERVICE_DOMAIN = "https://jobkok.netlify.app/applicant/auth";
    private final RecruitRepository recruitRepository;
    private final MemberService memberService;
    private final Response response;
    private final RedisTemplate<String, RecruitRes> redisTemplate;

    /*===========================
        채용폼 조회
    ===========================*/
    // 전체조회
    public ResponseEntity<?> selectALlRecruit(String memberEmail, boolean recruitStatus) {
        try {
            List<Recruit> recruitList = recruitRepository.findAllByMember_MemberEmailAndRecruitOngoing(memberEmail, recruitStatus);
            return checkRecruitRes(recruitList);
        } catch (RecruitException e) {
            e.printStackTrace();
            throw new RecruitException(ErrorCode.UNABLE_TO_GET_RECRUITFORM);
        }
    }

    /*===========================
        채용폼 검색
    ===========================*/
    public ResponseEntity<?> searchRecruit(String memberEmail, boolean recruitStatus, String recruitTitle) {
        try {
            List<Recruit> recruitList = recruitRepository.findAllByMember_MemberEmailAndRecruitOngoingAndRecruitTitleContains(memberEmail, recruitStatus, recruitTitle);
            return checkRecruitRes(recruitList);
        } catch (RecruitException e) {
            e.printStackTrace();
            throw new RecruitException(ErrorCode.UNABLE_TO_GET_RECRUITFORM);
        }
    }

    // 조회결과 확인 메소드
    private ResponseEntity<?> checkRecruitRes(List<Recruit> recruitList) {
        if (recruitList.size() == 0) {
            return response.fail(
                    ErrorCode.RECRUIT_FORM_NOT_FOUND.getMessage(),
                    ErrorCode.RECRUIT_FORM_NOT_FOUND.getStatus());
        }
        List<RecruitRes> recruitResList = new ArrayList<>();

        for (Recruit recruit : recruitList) {
            recruitResList.add(new RecruitRes(recruit));
        }

        return response.success(
                recruitResList,
                "Successfully SelectAll RecruitForm");
    }

    /*===========================
        채용폼 상세조회 : Detail
     ===========================*/
    public ResponseEntity<?> selectRecruitDetail(String memberEmail, Long recruitId) {
        try {
            Recruit recruit = recruitRepository.findByRecruitId(recruitId).orElseThrow(
                    () -> new RecruitException(
                            ErrorCode.RECRUIT_FORM_NOT_FOUND,
                            String.format("Requested RecruitFrom %d Not Found", recruitId)));
            // 채용기간 기반, 채용상태 조정
            recruit.adjustProcedure();

            // 채용상태가 변경된 값을 출력
            RecruitRes recruitRes = new RecruitRes(recruitRepository.save(recruit));

            //레디스에 가장 최근 본 채용폼으로 저장
            saveRecentRecruit(memberEmail, recruitRes);

            return response.success(
                    recruitRes,
                    "Successfully Get RecruitForm");

        } catch (RecruitException e) {
            e.printStackTrace();
            throw new RecruitException(ErrorCode.UNABLE_TO_GET_RECRUITFORM);
        }
    }

    /*===========================
        채용폼 수정
    ===========================*/
    @Transactional
    public ResponseEntity<?> editRecruit(RecruitReq req, Long recruitId) {
        // 기존에 등록된 Recruit 정보추출
        try {
            Recruit recruit = recruitRepository.findByRecruitId(recruitId).orElseThrow(
                    () -> new RecruitException(
                            ErrorCode.RECRUIT_FORM_NOT_FOUND,
                            String.format("Requested RecruitFrom %d Not Found", recruitId)
                    ));
            // 입력된 정보로 Entity 수정
            recruit.updateEntity(req);
            // 단계날짜 변경시, 채용상태 재설정
            recruit.adjustProcedure();

            // 수정된 내용 DB 저장
            RecruitRes recruitRes = new RecruitRes(recruitRepository.save(recruit));

            // 수정된 내용 Redis에 저장
            saveRecentRecruit(recruit.getMember().getMemberEmail(),recruitRes);

            // DB에 저장된 내용전달
            return response.success(
                    recruitRes,
                    "Successfully Edit RecruitForm");

        } catch (RecruitException e) {
            e.printStackTrace();
            throw new RecruitException(ErrorCode.FAIL_TO_EDIT_RECRUITFORM);
        }
    }

    /*===========================
        채용폼 신규등록
     ===========================*/
    @Transactional
    public ResponseEntity<?> registRecruit(RecruitReq req, String memberEmail) {
        try {
            // nullCheck at memberService
            Member member = memberService.loadMemberByMemberEmail(memberEmail);

            // PreSave for get recruit_id
            Recruit recruit = recruitRepository.save(new Recruit(req, member));
            recruit.setRecruitUrl(generateUrl(String.valueOf(recruit.getRecruitId())));
            recruit.adjustProcedure();

            // 등록내용 return
            RecruitRes recruitRes = RecruitRes.fromEntity(recruitRepository.save(recruit));

            // 신규 등록 내용 Redis에 저장
            saveRecentRecruit(recruit.getMember().getMemberEmail(),recruitRes);

            return response.success(
                    recruitRes,
                    "Successfully Save RecruitForm"
            );

        } catch (Exception e) {
            e.printStackTrace();
            throw new RecruitException(ErrorCode.FAIL_TO_REGIST_RECRUITFORM);
        }
    }

    /*===========================
        채용폼 삭제
    ===========================*/
    @Transactional
    public ResponseEntity<?> deleteRecruit(Long recruitId, String memberEmail) {
        try {
            Recruit recruit = recruitRepository.findByRecruitId(recruitId).orElseThrow(
                    () -> new RecruitException(
                            ErrorCode.RECRUIT_FORM_NOT_FOUND,
                            String.format("Requested RecruitFrom %d Not Found", recruitId)));

            // 채용폼 삭제상태로 변환 & 저장
            recruit.setRecruitDelete();
            RecruitRes recruitRes = RecruitRes.fromEntity(recruitRepository.save(recruit));

            String key = "recentRecruit::"+ memberEmail;
            redisTemplate.delete(key);

            // DB에 저장된 상태값 전송
            return response.success(
                    recruitRes,
                    "Successfully Delete RecruitForm");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RecruitException(ErrorCode.FAIL_TO_DELETE_RECRUITFORM);
        }
    }

    /*===========================
        Component
    ===========================*/
    // 채용폼 연결링크 생성
    public String generateUrl(String recruitId) {
        return SERVICE_DOMAIN + "/" + recruitId;
    }

   /*===========================
        최근 본 채용폼 저장
    ===========================*/
   @Transactional
   public void saveRecentRecruit(String memberEmail, RecruitRes recruitRes) {

       String key = "recentRecruit::"+ memberEmail;
       redisTemplate.delete(key);

       redisTemplate.opsForValue().set(key, recruitRes);
       redisTemplate.expireAt(key, Date.from(ZonedDateTime.now().plusDays(7).toInstant())); // 유효기간 TTL 일주일 설정
   }

   /*===========================
        최근 본 채용폼 상세조회
    ===========================*/
    @Transactional
    public ResponseEntity<?> findRecentRecruit(String memberEmail) {
        try {
            String key = "recentRecruit::"+ memberEmail;

            //Redis 저장 시에 해당 objectMapper를 적용했기 때문에 데이터를 가져올때도 변환해서 가져와야함
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // timestamp 형식 안따르도록 설정
            objectMapper.registerModules(new JavaTimeModule(), new Jdk8Module()); // LocalDateTime 매핑을 위해 모듈 활성화
            RecruitRes recruitRes = objectMapper.convertValue(redisTemplate.opsForValue().get(key), RecruitRes.class);

            if(recruitRes == null){ //최근 본 채용폼이 없을 시 가장 최근 등록된 채용폼 가져오도록 처리
                RecruitRes findRecruitRes = recruitRepository.findTopByMember_MemberEmailOrderByRecruitRegistedAt(memberEmail)
                        .map(RecruitRes::new)
                        .orElseThrow(
                                () -> new RecruitException(ErrorCode.RECRUIT_FORM_NOT_FOUND)
                        );

                setProcedure(findRecruitRes);

                return response.success(findRecruitRes);
            }

            setProcedure(recruitRes);

            return response.success(recruitRes);

        } catch (RecruitException e) {
            e.printStackTrace();
            throw new RecruitException(ErrorCode.UNABLE_TO_GET_RECRUITFORM);
        }
    }

    /*===========================
        RecruitRes
        채용단계 설정 메서드
    ===========================*/
    private static void setProcedure(RecruitRes recruitRes) {
        if(recruitRes.isOngoing()){
            LocalDateTime current = LocalDateTime.now();

            String procedure = (
                    current.isAfter(recruitRes.getDocsStart()) && current.isBefore(recruitRes.getDocsEnd()) ? Procedure.DOCS.name()
                            : current.isAfter(recruitRes.getMeetStart()) && (current.isBefore(recruitRes.getMeetEnd())) ? Procedure.MEET.name()
                            : current.isAfter(recruitRes.getConfirmStart()) && (current.isBefore(recruitRes.getConfirmEnd())) ? Procedure.CONFIRM.name()
                            : Procedure.ONHOLD.name()
            );
            recruitRes.setProcedure(procedure);
        } else {
            recruitRes.setProcedure(Procedure.CLOSED.name());
        }
    }
}
