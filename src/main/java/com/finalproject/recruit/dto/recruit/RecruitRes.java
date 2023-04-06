package com.finalproject.recruit.dto.recruit;

import com.finalproject.recruit.entity.Recruit;
import com.finalproject.recruit.repository.RecruitRepository;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecruitRes {

    /*===========================
        Data
    ===========================*/
    private Long id;
    private String uploader;
    private boolean type;
    private String title;
    private String contents;
    private String keywordStandard;
    private String resumeTitle;
    private String recruitUrl;
    //-------------------------------
    private boolean ongoing;
    private String procedure;
    //-------------------------------
    private LocalDateTime docsStart;
    private LocalDateTime docsEnd;
    private LocalDateTime meetStart;
    private LocalDateTime meetEnd;
    private LocalDateTime confirmStart;
    private LocalDateTime confirmEnd;
    private LocalDateTime updateAt;

    /*===========================
        Custom Constructor
    ===========================*/
    public RecruitRes(Recruit recruit){
        this.id = recruit.getRecruitId();
        this.uploader = recruit.getMember().getCompanyName();
        this.type = recruit.isRecruitType();
        this.title = recruit.getRecruitTitle();
        this.contents = recruit.getRecruitContent();
        this.keywordStandard = recruit.getKeywordStandard();
        this.resumeTitle = recruit.getResumeTitle();
        this.recruitUrl = recruit.getRecruitUrl();
        this.ongoing = recruit.isRecruitOngoing();
        this.procedure = recruit.getRecruitProcedure();
        this.docsStart = recruit.getDocsStart();
        this.docsEnd = recruit.getDocsEnd();
        this.meetStart = recruit.getMeetStart();
        this.meetEnd = recruit.getMeetEnd();
        this.confirmStart = recruit.getConfirmStart();
        this.confirmEnd = recruit.getConfirmEnd();
        if(recruit.getRecruitUpdateAt() != null){
            this.updateAt = recruit.getRecruitUpdateAt().toLocalDateTime();
        }
    }

    /*===========================
        Method
    ===========================*/

    // 채용폼 Entity resDTO 변환
    public static RecruitRes fromEntity(Recruit entity){
        return new RecruitRes(entity);
    }

}
