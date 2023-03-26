package com.finalproject.recruit.jisoo.service;

import com.finalproject.recruit.entity.Apply;
import com.finalproject.recruit.entity.Recruit;
import com.finalproject.recruit.jisoo.Mail;
import com.finalproject.recruit.jisoo.dto.ApplicantsRes;
import com.finalproject.recruit.jisoo.dto.RecentRecruitRes;
import com.finalproject.recruit.jisoo.repository.ApplyRepository;
import com.finalproject.recruit.jisoo.repository.MailRepository;
import com.finalproject.recruit.jisoo.repository.RecruitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class KeepService {

    private final ApplyRepository applyRepository;
    private final RecruitRepository recruitRepository;

    private final MailRepository mailRepository;

    public RecentRecruitRes recentRecruit() {
        Recruit recentRecruit = recruitRepository.findTopByRecruitDeleteIsFalseOrderByCreatedTimeDesc().orElseThrow(
                ()-> new IllegalArgumentException("recruit을 찾을 수 없습니다.")
        );
        return RecentRecruitRes.fromRecruit(recentRecruit);
    }

    public List<ApplicantsRes> dropApplicants(Long recruitId){
        List<Apply> nonPassApplicants = applyRepository.findByRecruitRecruitIdAndPassIsFalse(recruitId);
        return nonPassApplicants.stream()
                .map(apply -> ApplicantsRes.fromApply(
                        apply,
                        mailRepository.findTopByApplyApplyIdOrderByCreatedTimeDesc(apply.getApplyId())
                                .map(Mail::getCreatedTime)
                                .orElse(null),
                        "keywords" ))
                .collect(Collectors.toList());
    }
}
