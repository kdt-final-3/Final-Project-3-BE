package com.finalproject.recruit.service;

import com.finalproject.recruit.dto.Response;
import com.finalproject.recruit.dto.recruit.RecruitReq;
import com.finalproject.recruit.dto.recruit.RecruitRes;
import com.finalproject.recruit.entity.Member;
import com.finalproject.recruit.entity.Recruit;
import com.finalproject.recruit.exception.recruit.ErrorCode;
import com.finalproject.recruit.exception.recruit.RecruitException;
import com.finalproject.recruit.repository.RecruitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecruitService {
    private static final String SERVICE_DOMAIN = "https://www.jobkok.com/view";
    private final RecruitRepository recruitRepository;
    private final MemberService memberService;
    private final Response response;

    /*===========================
        채용폼 전체조회
    ===========================*/
    public ResponseEntity<?> selectALlRecruit(String memberEmail, boolean recruitStatus) {
        try {
            List<Recruit> recruitList = recruitRepository.findAllByMember_MemberEmailAndRecruitOngoing(memberEmail, recruitStatus);
            return checkRecruitRes(recruitList);
        } catch (Exception e) {
            e.printStackTrace();
            return response.fail("Unable to Process Your Request");
        }
    }

    /*===========================
        채용폼 검색
    ===========================*/
    public ResponseEntity<?> searchRecruit(String memberEmail, boolean recruitStatus, String recruitTitle) {
        try {
            List<Recruit> recruitList = recruitRepository.findAllByMember_MemberEmailAndRecruitOngoingAndRecruitTitleContains(memberEmail, recruitStatus, recruitTitle);
            return checkRecruitRes(recruitList);
        } catch (Exception e) {
            e.printStackTrace();
            return response.fail("Unable to Process Your Request");
        }
    }

    // 조회결과가 비어있는지 확인하는 메소드
    private ResponseEntity<?> checkRecruitRes(List<Recruit> recruitList) {
        if (recruitList.size() == 0) {
            return response.fail(
                    "RecruitForm Not Found",
                    HttpStatus.NOT_FOUND);
        }
        List<RecruitRes> recruitResList = new ArrayList<>();

        for (Recruit recruit : recruitList) {
            recruitResList.add(new RecruitRes(recruit));
        }

        return response.success(
                recruitResList,
                "Successfully SelectAll RecruitForm",
                HttpStatus.OK);
    }

    /*===========================
        채용폼 상세조회 : Detail
     ===========================*/
    public ResponseEntity<?> selectRecruitDetail(Long recruitId) {
        try {
            Recruit recruit = recruitRepository.findByRecruitId(recruitId).orElseThrow(
                    () -> new RecruitException(
                            ErrorCode.RECRUIT_FORM_NOT_FOUND,
                            String.format("Request %d RecruitFrom not found", recruitId))
            );
            // 채용기간 기반, 채용상태 조정
            recruit.adjustProcedure();

            // 채용상태가 변경된 값을 출력
            RecruitRes recruitRes = new RecruitRes(recruitRepository.save(recruit));
            return response.success(
                    recruitRes,
                    "Successfully Get RecruitForm",
                    HttpStatus.OK) ;

        } catch (RecruitException e) {
            e.printStackTrace();
            return response.fail(
                    "Unable to Get RecruitForm",
                    HttpStatus.BAD_REQUEST);
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
                            String.format("Request %d RecruitFrom not found", recruitId)
                    ));
            // 입력된 정보로 Entity 수정
            recruit.updateEntity(req);
            // 단계날짜 변경시, 채용상태 재설정
            recruit.adjustProcedure();

            // 수정된 내용 DB 저장
            RecruitRes recruitRes = new RecruitRes(recruitRepository.save(recruit));

            // DB에 저장된 내용전달
            return response.success(
                    recruitRes,
                    "Successfully Edit RecruitForm",
                    HttpStatus.OK
            );

        } catch (RecruitException e) {
            e.printStackTrace();
            return response.fail(
                    "Unable to Edit RecruitForm",
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    /*===========================
        채용폼 신규등록
     ===========================*/
    @Transactional
    public ResponseEntity<?> registRecruit(RecruitReq req, String memberEmail) {
        try {
            Member member = memberService.loadMemberByMemberEmail(memberEmail);
            Recruit recruit = recruitRepository.save(new Recruit(req, member));
            recruit.setRecruitUrl(generateUrl(String.valueOf(recruit.getRecruitId())));
            recruit.adjustProcedure();

            // 등록내용 return
            RecruitRes recruitRes = RecruitRes.fromEntity(recruitRepository.save(recruit));
            return response.success(
                    recruitRes,
                    "Successfully Save RecruitForm",
                    HttpStatus.OK
            );

        } catch (Exception e) {
            e.printStackTrace();
            return response.fail(
                    "Unable to Save RecruitForm",
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
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
                            String.format("Request %d RecruitFrom not found", recruitId)
                    ));
            // 채용폼 삭제상태로 변환 & 저장
            recruit.setRecruitDelete();
            RecruitRes recruitRes = RecruitRes.fromEntity(recruitRepository.save(recruit));

            // DB에 저장된 상태값 전송
            return response.success(
                    recruitRes,
                    "Successfully Delete RecruitForm",
                    HttpStatus.OK
            );

        } catch (Exception e) {
            e.printStackTrace();
            return response.fail(
                    "Unable to Delete RecruitForm",
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    /*===========================
        Component
    ===========================*/
    // 채용폼 연결링크 생성
    public String generateUrl(String recruitId) {
        return SERVICE_DOMAIN + "/" + recruitId;
    }
}
