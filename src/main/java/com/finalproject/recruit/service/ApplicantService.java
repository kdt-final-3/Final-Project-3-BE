package com.finalproject.recruit.service;

import com.finalproject.recruit.dto.Response;
import com.finalproject.recruit.entity.Apply;
import com.finalproject.recruit.entity.Recruit;
import com.finalproject.recruit.dto.applicant.ApplicationReq;
import com.finalproject.recruit.dto.applicant.PreRequired;
import com.finalproject.recruit.exception.recruit.ErrorCode;
import com.finalproject.recruit.exception.recruit.RecruitException;
import com.finalproject.recruit.repository.ApplyRepository;
import com.finalproject.recruit.repository.RecruitRepository;
import com.finalproject.recruit.repository.apply.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    private final Response response;

    /*===========================
        지원서 등록
    ===========================*/
    @Transactional
    public ResponseEntity<?> postApplication(ApplicationReq applicationReq) {
        try {
            if (!checkEmail(applicationReq.getApplyEmail(), applicationReq.getRecruitId())){
                return response.fail(
                        "Already apply current Applicant",
                        HttpStatus.CONFLICT
                );
            }
            Recruit recruit = recruitRepository.findByRecruitId(applicationReq.getRecruitId()).orElseThrow(
                    () -> new RecruitException(
                            ErrorCode.RECRUIT_FORM_NOT_FOUND,
                            String.format("Request %d RecruitFrom not found", applicationReq.getRecruitId())
                    )
            );

            Apply apply = applyRepository.save(applicationReq.toApply(recruit));
            apply.setPaperSubmit();

            activitiesRepository.save(applicationReq.toActivities(apply));
            awardsRepository.save(applicationReq.toAwards(apply));
            careerRepository.save(applicationReq.toCareer(apply));
            certificateRepository.save(applicationReq.toCertificate(apply));
            educationRepository.save(applicationReq.toEducation(apply));
            languageRepository.save(applicationReq.toLanguage(apply));

            return response.success(
                    apply,
                    "Successfully Submit an Application",
                    HttpStatus.OK
            );
        } catch (Exception e) {
            e.printStackTrace();
            return response.fail(
                    "Unable to Submit an Application",
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    /*===========================
        이메일 확인
    ===========================*/
    public Boolean checkEmail(String email, Long recruitId) {
        return !applyRepository.existsApplyByApplyEmailAndRecruitRecruitId(email, recruitId);
    }

    /*===========================
        채용폼 정보추출
    ===========================*/
    public ResponseEntity<?> preRequired(Long recruitId) {
        try{
            Recruit recruit = recruitRepository.findByRecruitId(recruitId).orElseThrow(
                    () -> new RecruitException(
                            ErrorCode.RECRUIT_FORM_NOT_FOUND,
                            String.format("Request %d RecruitFrom not found", recruitId))
            );
            PreRequired preRequired = PreRequired.fromRecruit(recruit);
            return response.success(
                    preRequired,
                    "Successfully Get RecruitForm Info",
                    HttpStatus.OK
            );
        }catch (RecruitException e){
            e.printStackTrace();
            return response.fail(
                    "Unable to Get RecruitForm Info",
                    HttpStatus.BAD_REQUEST);
        }
    }
}
