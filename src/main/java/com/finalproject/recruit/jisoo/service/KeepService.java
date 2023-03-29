package com.finalproject.recruit.jisoo.service;

import com.finalproject.recruit.dto.Response;
import com.finalproject.recruit.entity.Apply;
import com.finalproject.recruit.entity.Recruit;
import com.finalproject.recruit.jisoo.Mail;
import com.finalproject.recruit.jisoo.dto.ApplicantsRes;
import com.finalproject.recruit.jisoo.repository.ApplyRepository;
import com.finalproject.recruit.jisoo.repository.MailRepository;
import com.finalproject.recruit.jisoo.repository.RecruitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class KeepService {

    private final ApplyRepository applyRepository;
    private final RecruitRepository recruitRepository;

    private final MailRepository mailRepository;

    private final Response response;

    public ResponseEntity<?> recentRecruit(String email) {
        Recruit recentRecruit = recruitRepository.findTopByRecruitDeleteIsFalseAndMemberMemberEmailOrderByCreatedTimeDesc(email).orElse(null);
        if (recentRecruit == null) {
            return response.fail("채용공고가 존재하지 않습니다.");
        }
        return dropApplicants(recentRecruit.getRecruitId());
    }

    public ResponseEntity<?> dropApplicants(Long recruitId){
        List<Apply> nonPassApplicants = applyRepository.findByRecruitRecruitIdAndFailApplyIsTrue(recruitId);
        List<ApplicantsRes> applicantsResList = nonPassApplicants.stream()
                .map(apply -> ApplicantsRes.fromApply(
                        apply,
                        mailRepository.findTopByApplyApplyIdOrderByCreatedTimeDesc(apply.getApplyId())
                                .map(Mail::getCreatedTime)
                                .orElse(null)))
                .collect(Collectors.toList());
        return response.success(applicantsResList);
    }

    public ResponseEntity<?> eternalDeleteApplicants(Long recruitId) {
        List<Apply> deletedApplicants = applyRepository.findByRecruitRecruitIdAndApplyDeleteIsTrue(recruitId);
        List<ApplicantsRes> applicantsResList = deletedApplicants.stream()
                .map(apply -> ApplicantsRes.fromApply(
                        apply,
                        mailRepository.findTopByApplyApplyIdOrderByCreatedTimeDesc(apply.getApplyId())
                                .map(Mail::getCreatedTime)
                                .orElse(null)))
                .collect(Collectors.toList());
        return response.success(applicantsResList);
    }
}
