package com.finalproject.recruit.jisoo.service;

import com.finalproject.recruit.entity.Apply;
import com.finalproject.recruit.entity.Recruit;
import com.finalproject.recruit.jisoo.dto.ApplicationReq;
import com.finalproject.recruit.jisoo.repository.ApplyRepository;
import com.finalproject.recruit.jisoo.repository.RecruitRepository;
import com.finalproject.recruit.jisoo.repository.apply.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ApplicantService {

    private final ApplyRepository applyRepository;
    private final RecruitRepository recruitRepository;

    private final ActivitiesRepository activitiesRepository;
    private final AwardsRepository awardsRepository;
    private final CareerRepository careerRepository;
    private final CertificateRepository certificateRepository;
    private final EducationRepository educationRepository;
    private final LanguageRepository languageRepository;
    private final MilitaryRepository militaryRepository;

    @Transactional
    public String postApplication(ApplicationReq applicationReq) {
        try {
            System.out.println(applicationReq.getRecruitId());
            Recruit recruit = recruitRepository.findByRecruitIdAndRecruitDeleteIsFalse(applicationReq.getRecruitId()).orElseThrow(
                    ()-> new IllegalArgumentException("해당 채용공고가 존재하지 않습니다.")
            );
            Apply apply = applyRepository.save(applicationReq.toApply(recruit));

            System.out.println(apply.getApplyName());
            activitiesRepository.save(applicationReq.toActivities(apply));
            awardsRepository.save(applicationReq.toAwards(apply));
            careerRepository.save(applicationReq.toCareer(apply));
            certificateRepository.save(applicationReq.toCertificate(apply));
            educationRepository.save(applicationReq.toEducation(apply));
            languageRepository.save(applicationReq.toLanguage(apply));
            militaryRepository.save(applicationReq.toMilitary(apply));
            return "success";
        } catch (Exception e){
            return "failed";
        }


    }
}
