package com.finalproject.recruit.dto.keep;

import com.finalproject.recruit.entity.Recruit;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
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
       private LocalDateTime docsStart;
       private LocalDateTime docsEnd;
       private LocalDateTime meetStart;
       private LocalDateTime meetEnd;
       private LocalDateTime confirmStart;
       private LocalDateTime confirmEnd;
       private LocalDateTime recruitRegistedAt;

       public static RecentRecruitRes fromRecruit(Recruit recruit){
              return RecentRecruitRes.builder()
                      .recruitId(recruit.getRecruitId())
                      .recruitTitle(recruit.getRecruitTitle())
                      .recruitContent(recruit.getRecruitContent())
                      .recruitProcedure(recruit.getRecruitProcedure())
                      .keywordStandard(recruit.getKeywordStandard())
                      .docsStart(recruit.getDocsStart())
                      .docsEnd(recruit.getDocsEnd())
                      .meetStart(recruit.getMeetStart())
                      .meetEnd(recruit.getMeetEnd())
                      .confirmStart(recruit.getConfirmStart())
                      .confirmEnd(recruit.getConfirmEnd())
                      .recruitRegistedAt(recruit.getRecruitRegistedAt())
                      .build();
       }
}
