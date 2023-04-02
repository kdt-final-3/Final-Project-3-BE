package com.finalproject.recruit.service;

import com.finalproject.recruit.dto.recruit.RecruitReq;
import com.finalproject.recruit.dto.recruit.RecruitRes;
import com.finalproject.recruit.entity.Member;
import com.finalproject.recruit.entity.Recruit;
import com.finalproject.recruit.exception.recruit.ErrorCode;
import com.finalproject.recruit.exception.recruit.RecruitException;
import com.finalproject.recruit.repository.RecruitRepository;
import com.finalproject.recruit.service.archive.MemberServiceArch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecruitService {
    private static final String SERVICE_DOMAIN = "https://www.jobkok.com/view";
    private final RecruitRepository recruitRepository;
    private final MemberServiceArch memberServiceArch;

    /*===========================
        채용폼 전체조회
    ===========================*/
    public List<RecruitRes> selectALlRecruit(String memberEmail, boolean recruitStatus) {
        try {
            List<Recruit> recruitList = recruitRepository.findAllByMember_MemberEmailAndRecruitOngoing(memberEmail, recruitStatus);
            return checkRecruitRes(recruitList);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /*===========================
        채용폼 검색
    ===========================*/
    public List<RecruitRes> searchRecruit(String memberEmail, boolean recruitStatus, String recruitTitle){
        try{
            List<Recruit> recruitList = recruitRepository.findAllByAndMemberMemberEmailAndRecruitOngoingAndRecruitTitleContains(memberEmail, recruitStatus, recruitTitle);
            return checkRecruitRes(recruitList);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    // 조회결과가 비어있는지 확인하는 메소드
    private List<RecruitRes> checkRecruitRes(List<Recruit> recruitList) {
        if(recruitList.size() == 0){ return null; }
        List<RecruitRes> recruitResList = new ArrayList<>();

        for (Recruit recruit : recruitList) {
            recruitResList.add(new RecruitRes(recruit));
        }

        return recruitResList;
    }

    /*===========================
        채용폼 상세조회 : Detail
     ===========================*/
    public RecruitRes selectRecruitDetail(Long recruitId){
        try{
            Recruit recruit = recruitRepository.findByRecruitId(recruitId).orElseThrow(
                    () -> new RecruitException(
                            ErrorCode.RECRUIT_FORM_NOT_FOUND,
                            String.format("Request %d RecruitFrom not found", recruitId))
            );
            // 채용기간 기반, 채용상태 조정
            recruit.adjustProcedure();

            // 채용상태가 변경된 값을 출력
            return new RecruitRes(recruitRepository.save(recruit));
        }catch(RecruitException e){
            return null;
        }
    }

    /*===========================
        채용폼 수정
    ===========================*/
    @Transactional
    public RecruitRes editRecruit(RecruitReq req, Long recruitId){
        // 기존에 등록된 Recruit 정보추출
        try{
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
            recruit = recruitRepository.save(recruit);

            // DB에 저장된 내용확인
            return RecruitRes.fromEntity(recruit);

        }catch(RecruitException e){
            return null;
        }
    }

    /*===========================
        채용폼 신규등록
     ===========================*/
    @Transactional
    public RecruitRes registRecruit(RecruitReq req, String memberEmail){
        try{
            Member member = memberServiceArch.loadMemberByMemberEmail(memberEmail);
            Recruit recruit = recruitRepository.save(new Recruit(req, member));
            recruit.setRecruitUrl(generateUrl(String.valueOf(recruit.getRecruitId())));
            recruit.adjustProcedure();

            return RecruitRes.fromEntity(recruitRepository.save(recruit));
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /*===========================
        채용폼 삭제
    ===========================*/
    @Transactional
    public String deleteRecruit(Long recruitId){
        try{
            Recruit recruit = recruitRepository.findByRecruitId(recruitId).orElseThrow(
                    () -> new RecruitException(
                            ErrorCode.RECRUIT_FORM_NOT_FOUND,
                            String.format("Request %d RecruitFrom not found", recruitId)
                    ));
            // 채용폼 삭제상태로 변환 & 저장
            recruit.setRecruitDelete();
            recruit = recruitRepository.save(recruit);

            // DB에 저장된 상태값 전송
            return "Recruit From Delete Request at : " + recruit.getRecruitDeleteAt();

        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /*===========================
        Component
    ===========================*/
    // 채용폼 연결링크 생성
    public String generateUrl(String recruitId){
        return SERVICE_DOMAIN + "/" + recruitId;
    }
}
