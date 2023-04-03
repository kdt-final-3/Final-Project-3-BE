package com.finalproject.recruit.service;

import com.finalproject.recruit.dto.Response;
import com.finalproject.recruit.dto.keep.ApplicantsRes;
import com.finalproject.recruit.entity.Apply;
import com.finalproject.recruit.entity.Mail;
import com.finalproject.recruit.entity.Recruit;
import com.finalproject.recruit.repository.ApplyRepository;
import com.finalproject.recruit.repository.MailRepository;
import com.finalproject.recruit.repository.RecruitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KeepService {

    private final ApplyRepository applyRepository;
    private final RecruitRepository recruitRepository;

    private final MailRepository mailRepository;

    private final Response response;

    public ResponseEntity<?> recentRecruit(String email, Pageable pageable) {
        Recruit recentRecruit = recruitRepository.findTopByAndMemberMemberEmailOrderByRecruitRegistedAt(email).orElse(null);
        if (recentRecruit == null) {
            return response.fail("채용공고가 존재하지 않습니다.");
        }
        return dropApplicants(recentRecruit.getRecruitId(), pageable);
    }

    public ResponseEntity<?> dropApplicants(Long recruitId, Pageable pageable){
        Page<Apply> nonPassApplicants = applyRepository.findByRecruitRecruitIdAndFailApplyIsTrue(recruitId,pageable);
        Page<ApplicantsRes> applicantsResList = nonPassApplicants
                .map(apply -> ApplicantsRes.fromApply(
                        apply,
                        mailRepository.findTopByApplyApplyIdOrderByCreatedTimeDesc(apply.getApplyId())
                                .map(Mail::getCreatedTime)
                                .orElse(null)));
        return response.success(applicantsResList);
    }

    public ResponseEntity<?> eternalDeleteApplicants(Long recruitId, Pageable pageable) {
        Page<Apply> deletedApplicants = applyRepository.findByRecruitRecruitIdAndApplyDeleteIsTrue(recruitId, pageable);
        Page<ApplicantsRes> applicantsResList = deletedApplicants
                .map(apply -> ApplicantsRes.fromApply(
                        apply,
                        mailRepository.findTopByApplyApplyIdOrderByCreatedTimeDesc(apply.getApplyId())
                                .map(Mail::getCreatedTime)
                                .orElse(null)));
        return response.success(applicantsResList);
    }
}
