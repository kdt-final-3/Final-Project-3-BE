package com.finalproject.recruit.dto.applicant;

import com.finalproject.recruit.entity.Apply;
import com.finalproject.recruit.entity.Recruit;
import com.finalproject.recruit.entity.apply.*;
import com.finalproject.recruit.parameter.Keywords;

import com.finalproject.recruit.parameter.MilitaryEnum;
import com.finalproject.recruit.parameter.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class ApplicationReq {

        private Long recruitId;
        private String applyName;
        private String applyPhone;
        private String applyEmail;

      //  private Long applyId;

        private String resumeContent;
        private String applyPortfolio;

        private String applyResume;

        private String keywordsReq;

        /**
         * 우대사항 관련
         * */
        private boolean disorder;
        private boolean veteran;
        private boolean employment;
        private boolean terms;
        private MilitaryEnum militaryEnum;


        private String eduName;
        private EduYear eduYear;
        private String eduMajor;
        private EduStatus eduStatus;
        private LocalDateTime eduStart;
        private LocalDateTime eduEnd;


        private String careerName;
        private LocalDateTime careerStart;
        private LocalDateTime careerEnd;
        private String careerDetail;

        private String activitiesTitle;
        private String activitiesContent;
        private LocalDateTime activitiesStart;
        private LocalDateTime activitiesEnd;

        private String certificateName;
        private LocalDateTime certificateDate;
        private String certificatePublisher;

        private String awardsName;
        private LocalDateTime awardsDate;
        private String awardsCompany;

        private String languageName;
        private String languageSkill;
        private LanguageLevel languageLevel;



        public Apply toApply(Recruit recruit){
                return Apply.builder()
                        .recruit(recruit)
                        .applyName(applyName)
                        .applyPhone(applyPhone)
                        .applyEmail(applyEmail)
                        .resumeContent(resumeContent)
                        .applyPortfolio(applyPortfolio)
                        .applyResume(applyResume)
                        .keywordSelect(keywordsReq)
                        .disorder(disorder)
                        .veteran(veteran)
                        .employment(employment)
                        .terms(terms)
                        .military(militaryEnum)
                        .build();
        }


        public String keywordToString(List<Boolean> req){
                String result = "";
                Keywords[] values = Keywords.values();
                for (int i = 0; i < 10; i++) {
                        if (req.get(i)==Boolean.TRUE){
                                result+=values[i]+" ";
                        }
                }
                return result;
        }

        public Activities toActivities(Apply apply){
                return Activities.builder()
                        .apply(apply)
                        .activitiesTitle(activitiesTitle)
                        .activitiesContent(activitiesContent)
                        .activitiesStart(activitiesStart)
                        .activitiesEnd(activitiesEnd)
                        .build();
        }

        public Awards toAwards(Apply apply){
                return Awards.builder()
                        .apply(apply)
                        .awardsName(awardsName)
                        .awardsCompany(awardsCompany)
                        .awardsDate(awardsDate)
                        .build();
        }

        public Career toCareer(Apply apply){
                return Career.builder()
                        .apply(apply)
                        .careerName(careerName)
                        .careerDetail(careerDetail)
                        .careerStart(careerStart)
                        .careerEnd(careerEnd)
                        .build();
        }

        public Certificate toCertificate(Apply apply){
                return Certificate.builder()
                        .apply(apply)
                        .certificateName(certificateName)
                        .certificatePublisher(certificatePublisher)
                        .certificateDate(certificateDate)
                        .build();
        }

        public Education toEducation(Apply apply){
                return Education.builder()
                        .apply(apply)
                        .eduName(eduName)
                        .eduStatus(eduStatus)
                        .eduMajor(eduMajor)
                        .eduStart(eduStart)
                        .eduEnd(eduEnd)
                        .build();
        }

        public Language toLanguage(Apply apply){
                return Language.builder()
                        .apply(apply)
                        .languageName(languageName)
                        .languageSkill(languageSkill)
                        .languageLevel(languageLevel)
                        .build();
        }


}
