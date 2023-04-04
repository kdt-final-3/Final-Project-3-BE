package com.finalproject.recruit.service;

import com.finalproject.recruit.dto.Response;
import com.finalproject.recruit.dto.keep.ApplicantsRes;
import com.finalproject.recruit.entity.Apply;
import com.finalproject.recruit.entity.Mail;
import com.finalproject.recruit.entity.Recruit;
import com.finalproject.recruit.exception.keep.ErrorCode;
import com.finalproject.recruit.exception.keep.KeepException;
import com.finalproject.recruit.repository.ApplyRepository;
import com.finalproject.recruit.repository.MailRepository;
import com.finalproject.recruit.repository.RecruitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                    () -> new KeepException(ErrorCode.RECRUIT_FORM_NOT_FOUND));
            return dropApplicants(recentRecruit.getRecruitId(), pageable);

        }catch(KeepException e) {
            e.printStackTrace();
            throw new KeepException(ErrorCode.UNABLE_TO_PROCESS_REQUEST);
        }
    }

    /*===========================
        탈락 인재보관
    ===========================*/
    @Transactional
    public ResponseEntity<?> dropApplicants(Long recruitId, Pageable pageable){
        try{
            Page<Apply> nonPassApplicants = applyRepository.findByRecruitRecruitIdAndFailApplyIsTrue(recruitId,pageable);
            // empty check
            if(!hasContents(nonPassApplicants)){
                return response.fail(
                        ErrorCode.APPLY_NOT_FOUND.getMessage(),
                        ErrorCode.APPLY_NOT_FOUND.getStatus());
            }

            Page<ApplicantsRes> applicantsResList = nonPassApplicants
                    .map(apply -> ApplicantsRes.fromApply(
                            apply,
                            mailRepository.findTopByApplyApplyIdOrderByCreatedTimeDesc(apply.getApplyId())
                                    .map(Mail::getCreatedTime)
                                    .orElse(null)));
            // empty check()
            if(!hasContents(applicantsResList)){
                return response.fail(
                        ErrorCode.APPLICANTS_NOT_FOUND.getMessage(),
                        ErrorCode.APPLICANTS_NOT_FOUND.getStatus());
            }

            return response.success(
                    applicantsResList,
                    "Successfully Get Drop Applicants List"
            );

        }catch(KeepException e){
            e.printStackTrace();
            throw new KeepException(ErrorCode.UNABLE_TO_PROCESS_REQUEST);
        }
    }

    /*===========================
        영구삭제 인재보관
    ===========================*/
    public ResponseEntity<?> eternalDeleteApplicants(Long recruitId, Pageable pageable) {
        try{
            Page<Apply> deletedApplicants = applyRepository.findByRecruitRecruitIdAndApplyDeleteIsTrue(recruitId, pageable);
            // empty check
            if(!hasContents(deletedApplicants)){
                return response.fail(
                        ErrorCode.APPLY_NOT_FOUND.getMessage(),
                        ErrorCode.APPLY_NOT_FOUND.getStatus());
            }

            Page<ApplicantsRes> applicantsResList = deletedApplicants
                    .map(apply -> ApplicantsRes.fromApply(
                            apply,
                            mailRepository.findTopByApplyApplyIdOrderByCreatedTimeDesc(apply.getApplyId())
                                    .map(Mail::getCreatedTime)
                                    .orElse(null)));
            // empty check()
            if(!hasContents(applicantsResList)){
                return response.fail(
                        ErrorCode.APPLICANTS_NOT_FOUND.getMessage(),
                        ErrorCode.APPLICANTS_NOT_FOUND.getStatus());
            }

            return response.success(
                    applicantsResList,
                    "Successfully Get Deleted Applicants List");
        }catch(KeepException e){
            e.printStackTrace();
            throw new KeepException(ErrorCode.UNABLE_TO_PROCESS_REQUEST);
        }
    }

    /*===========================
        Empty Check
    ===========================*/
    public boolean hasContents(Page<?> input){
        return input.hasContent();
    }
}
