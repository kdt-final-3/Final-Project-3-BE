package com.finalproject.recruit.jisoo.dto;

import com.finalproject.recruit.entity.Recruit;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class RecentRecruitRes {
       private Long recruitId;
       private String recruitTitle;
       private String recruitContent;
       private String recruitProcedure;
       private String keywordStandard; //배열로
       private LocalDateTime startDate;
       private LocalDateTime endDate;
       private LocalDateTime meetStart;
       private LocalDateTime meetEnd;
       private LocalDateTime goal;
       private LocalDateTime createdTime;

       public static RecentRecruitRes fromRecruit(Recruit recruit){
              return RecentRecruitRes.builder()
                      .recruitId(recruit.getRecruitId())
                      .recruitTitle(recruit.getRecruitTitle())
                      .recruitContent(recruit.getRecruitContent())
                      .recruitProcedure(recruit.getRecruitProcedure())
                      .keywordStandard(recruit.getKeywordStandard())
                      .startDate(recruit.getStartDate())
                      .endDate(recruit.getEndDate())
                      .meetStart(recruit.getMeetStart())
                      .meetEnd(recruit.getMeetEnd())
                      .goal(recruit.getGoal())
                      .createdTime(recruit.getCreatedTime())
                      .build();
       }
}
