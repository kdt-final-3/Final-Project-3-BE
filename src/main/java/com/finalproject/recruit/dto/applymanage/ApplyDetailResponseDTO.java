package com.finalproject.recruit.dto.applymanage;

import com.finalproject.recruit.entity.Apply;
import com.finalproject.recruit.parameter.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApplyDetailResponseDTO {
    Long recruitId;
    String applyName;
    String applyPhone;
    String applyEmail;
    String resumeContent;
    Boolean disorder;
    Boolean veteran;
    Boolean employment;
    Boolean terms;
    MilitaryEnum militaryEnum;
    String applyPortfolio;
    String applyResume;
    ApplyProcedure applyProcedure;
    String evaluation;
    Boolean pass;
    Boolean failApply;
    LocalDateTime createdTime;
    LocalDateTime checkApply;
    LocalDateTime meeting;
    LocalDateTime passDay;
    Boolean applyDelete;
    Boolean wish;
    String careerName;
    LocalDateTime careerStart;
    LocalDateTime careerEnd;
    String careerDetail;
    String eduName;
    EduYear eduYear;
    String eduMajor;
    EduStatus eduStatus;
    LocalDateTime eduStart;
    LocalDateTime eduEnd;
    String languageName;
    LanguageLevel languageLevel;
    String certificateName;
    LocalDateTime certificateDate;
    String certificatePublisher;
    String activitesTitle;
    String activitesContent;
    LocalDateTime activitesStart;
    LocalDateTime activitesEnd;
    String awardsName;
    LocalDateTime awardsDate;
    String awardsCompany;
    String keywords;
    List<String> keywordList;

    public ApplyDetailResponseDTO(Apply apply) {
        this.recruitId = apply.getRecruit().getRecruitId();
        this.applyName = apply.getApplyName();
        this.applyPhone = apply.getApplyPhone();
        this.applyEmail = apply.getApplyEmail();
        this.resumeContent = apply.getResumeContent();
        this.disorder = apply.isDisorder();
        this.veteran = apply.isVeteran();
        this.employment = apply.isEmployment();
        this.terms = apply.isTerms();
        this.militaryEnum = apply.getMilitary();
        this.applyPortfolio = apply.getApplyPortfolio();
        this.applyResume = apply.getApplyResume();
        this.applyProcedure = apply.getApplyProcedure();
        this.evaluation = apply.getEvaluation();
        this.pass = apply.isPass();
        this.failApply = apply.isFailApply();
        this.createdTime = apply.getCreatedTime();
        this.checkApply = apply.getCheckApply();
        this.meeting = apply.getMeeting();
        this.passDay = apply.getPassDay();
        this.applyDelete = apply.isApplyDelete();
        this.wish = apply.isWish();
        this.careerName = apply.getCareer().getCareerName();
        this.careerStart = apply.getCareer().getCareerStart();
        this.careerEnd = apply.getCareer().getCareerEnd();
        this.careerDetail = apply.getCareer().getCareerDetail();
        this.eduName = apply.getEducation().getEduName();
        this.eduYear = apply.getEducation().getEduYear();
        this.eduMajor = apply.getEducation().getEduMajor();
        this.eduStatus = apply.getEducation().getEduStatus();
        this.eduStart = apply.getEducation().getEduStart();
        this.eduEnd = apply.getEducation().getEduEnd();
        this.languageName = apply.getLanguage().getLanguageName();
        this.languageLevel = apply.getLanguage().getLanguageLevel();
        this.certificateName = apply.getCertificate().getCertificateName();
        this.certificateDate = apply.getCertificate().getCertificateDate();
        this.certificatePublisher = apply.getCertificate().getCertificatePublisher();
        this.activitesTitle = apply.getActivities().getActivitiesTitle();
        this.activitesContent = apply.getActivities().getActivitiesContent();
        this.activitesStart = apply.getActivities().getActivitiesStart();
        this.activitesEnd = apply.getActivities().getActivitiesEnd();
        this.awardsName = apply.getAwards().getAwardsName();
        this.awardsDate = apply.getAwards().getAwardsDate();
        this.awardsCompany = apply.getAwards().getAwardsCompany();
        this.keywords = apply.getKeywordSelect();
    }
}
