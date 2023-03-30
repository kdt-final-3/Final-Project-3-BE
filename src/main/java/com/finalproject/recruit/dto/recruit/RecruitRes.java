package com.finalproject.recruit.dto.Recruit;

import com.finalproject.recruit.entity.recruit.Recruit;
import com.finalproject.recruit.repository.recruit.RecruitRepository;
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
    //private List<LocalDateTime> timeline;

    /*===========================
        Custom Constructor
    ===========================*/
    public RecruitRes(Recruit recruit){
        this.id = recruit.getRecruitId();
        //this.uploader = recruit.getMember().getCompanyName();
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
    }

    /*===========================
        Method
    ===========================*/

    // 채용폼 Entity resDTO 변환
    public static RecruitRes fromEntity(Recruit entity){
        return new RecruitRes(entity);
    }


    /*===========================
        Archive
    ===========================
    public void updateTimeline(){
        List<LocalDateTime> timeline = new ArrayList<>();
        timeline.add(docsStart);
        timeline.add(docsEnd);
        timeline.add(meetStart);
        timeline.add(meetEnd);
        timeline.add(confirmStart);
        timeline.add(confirmEnd);
    }
     */
}
