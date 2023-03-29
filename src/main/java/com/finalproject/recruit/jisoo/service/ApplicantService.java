package com.finalproject.recruit.jisoo.service;

import com.finalproject.recruit.dto.Response;
import com.finalproject.recruit.entity.Apply;
import com.finalproject.recruit.entity.Recruit;
import com.finalproject.recruit.jisoo.dto.ApplicantInfoReq;
import com.finalproject.recruit.jisoo.dto.ApplicationReq;
import com.finalproject.recruit.jisoo.repository.ApplyRepository;
import com.finalproject.recruit.jisoo.repository.RecruitRepository;
import com.finalproject.recruit.jisoo.repository.apply.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

    private final Response response;

    @Transactional
    public ResponseEntity<?> postApplication(ApplicationReq applicationReq) {
        try {
            if (!checkEmail(applicationReq.getApplyEmail(), applicationReq.getRecruitId())){
                return response.fail("이미 현 채용공고에 지원하셨습니다.");
            }
            Recruit recruit = recruitRepository.findByRecruitIdAndRecruitDeleteIsFalse(applicationReq.getRecruitId()).orElseThrow(
                    () -> new IllegalArgumentException("해당 채용공고가 존재하지 않습니다.")
            );
            Apply apply = applyRepository.save(applicationReq.toApply(recruit));
            //Apply apply = applyRepository.findByApplyId(applicationReq.getApplyId()).orElseThrow(()-> new IllegalArgumentException("지원자가 존재하지 않습니다."));
            activitiesRepository.save(applicationReq.toActivities(apply));
            awardsRepository.save(applicationReq.toAwards(apply));
            careerRepository.save(applicationReq.toCareer(apply));
            certificateRepository.save(applicationReq.toCertificate(apply));
            educationRepository.save(applicationReq.toEducation(apply));
            languageRepository.save(applicationReq.toLanguage(apply));
            militaryRepository.save(applicationReq.toMilitary(apply));
            return response.success("지원서 제출에 성공했습니다.");
        } catch (Exception e) {
            return response.fail("지원서 제출에 실패했습니다.");
        }


    }

    public Long postInfo(ApplicantInfoReq applicantInfoReq) {
        Recruit recruit = recruitRepository.findByRecruitIdAndRecruitDeleteIsFalse(applicantInfoReq.getRecruitId()).orElseThrow(
                () -> new IllegalArgumentException("해당 채용공고가 존재하지 않습니다.")
        );
        Apply apply = applyRepository.save(applicantInfoReq.toApply(recruit));
        return apply.getApplyId();
    }

    public Boolean checkEmail(String email, Long recruitId) {
        return !applyRepository.existsApplyByApplyEmailAndRecruitRecruitId(email, recruitId);
    }
}
