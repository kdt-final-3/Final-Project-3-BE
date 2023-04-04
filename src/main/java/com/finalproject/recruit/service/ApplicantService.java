package com.finalproject.recruit.service;

import com.finalproject.recruit.dto.Response;
import com.finalproject.recruit.entity.Apply;
import com.finalproject.recruit.entity.Recruit;
import com.finalproject.recruit.dto.applicant.ApplicationReq;
import com.finalproject.recruit.dto.applicant.PreRequired;
import com.finalproject.recruit.exception.applicant.ApplicantException;
import com.finalproject.recruit.exception.applicant.ErrorCode;
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
                        ErrorCode.EMAIL_NOT_FOUND.getMessage(),
                        ErrorCode.EMAIL_NOT_FOUND.getStatus());
            }
            Recruit recruit = recruitRepository.findByRecruitId(applicationReq.getRecruitId()).orElseThrow(
                    () -> new ApplicantException(ErrorCode.RECRUIT_FORM_NOT_FOUND)
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
                    "Successfully Submit an Application");
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApplicantException(ErrorCode.FAIL_TO_SUBMIT_APPLICANT);
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
                    () -> new ApplicantException(
                            ErrorCode.RECRUIT_FORM_NOT_FOUND,
                            String.format("Request RecruitFrom %d Not Found", recruitId))
            );
            PreRequired preRequired = PreRequired.fromRecruit(recruit);
            return response.success(
                    preRequired,
                    "Successfully Get RecruitForm Info");
        }catch (ApplicantException e) {
            e.printStackTrace();
            throw new ApplicantException(ErrorCode.UNABLE_TO_PROCESS_REQUEST);
        }
    }
}
