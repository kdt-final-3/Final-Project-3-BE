package com.finalproject.recruit.service;

import com.finalproject.recruit.dto.Response;
import com.finalproject.recruit.dto.keep.ApplicantsRes;
import com.finalproject.recruit.entity.Apply;
import com.finalproject.recruit.entity.Mail;
import com.finalproject.recruit.entity.Recruit;
import com.finalproject.recruit.exception.recruit.ErrorCode;
import com.finalproject.recruit.exception.recruit.RecruitException;
import com.finalproject.recruit.repository.ApplyRepository;
import com.finalproject.recruit.repository.MailRepository;
import com.finalproject.recruit.repository.RecruitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KeepService {

    private final ApplyRepository applyRepository;
    private final RecruitRepository recruitRepository;
    private final MailRepository mailRepository;
    private final Response response;

    /*===========================
        최근 채용폼 조회
    ===========================*/
    public ResponseEntity<?> recentRecruit(String email, Pageable pageable) {
        try{
            Recruit recentRecruit = recruitRepository.findTopByAndMemberMemberEmailOrderByRecruitRegistedAt(email).orElseThrow(
                    () -> new RecruitException(ErrorCode.RECRUIT_FORM_NOT_FOUND));
            return dropApplicants(recentRecruit.getRecruitId(), pageable);

        }catch(RecruitException e){
            e.printStackTrace();
            return response.fail(
                    "Unable to Process Your Request",
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    /*===========================
        탈락 인재보관
    ===========================*/
    public ResponseEntity<?> dropApplicants(Long recruitId, Pageable pageable){
        Page<Apply> nonPassApplicants = applyRepository.findByRecruitRecruitIdAndFailApplyIsTrue(recruitId,pageable);

        if(hasApplicants(nonPassApplicants)){
            Page<ApplicantsRes> applicantsResList = nonPassApplicants
                    .map(apply -> ApplicantsRes.fromApply(
                            apply,
                            mailRepository.findTopByApplyApplyIdOrderByCreatedTimeDesc(apply.getApplyId())
                                    .map(Mail::getCreatedTime)
                                    .orElse(null)));    // 최근 메세지 연락이 없으면 Null을 넣는 로직으로 보여 Exception 추가X
            return response.success(applicantsResList);
        }
        return response.fail(
                "NonPassApplicants Not Found",
                HttpStatus.NOT_FOUND
        );
    }

    /*===========================
        영구삭제 인재보관
    ===========================*/
    public ResponseEntity<?> eternalDeleteApplicants(Long recruitId, Pageable pageable) {
        Page<Apply> deletedApplicants = applyRepository.findByRecruitRecruitIdAndApplyDeleteIsTrue(recruitId, pageable);

        if(hasApplicants(deletedApplicants)){
            Page<ApplicantsRes> applicantsResList = deletedApplicants
                    .map(apply -> ApplicantsRes.fromApply(
                            apply,
                            mailRepository.findTopByApplyApplyIdOrderByCreatedTimeDesc(apply.getApplyId())
                                    .map(Mail::getCreatedTime)
                                    .orElse(null)));    // 최근 메세지 연락이 없으면 Null을 넣는 로직으로 보여 Exception 추가X
            return response.success(applicantsResList);
        }
        return response.fail(
                "EternalDeleteApplicants Not Found",
                HttpStatus.NOT_FOUND
        );

    }

    /*===========================
        Empty Check
    ===========================*/
    public boolean hasApplicants(Page<Apply> input){
        return input.hasContent();
    }
}
