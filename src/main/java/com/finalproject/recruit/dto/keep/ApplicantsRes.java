package com.finalproject.recruit.dto.keep;

import com.finalproject.recruit.entity.Apply;
import com.finalproject.recruit.parameter.ApplyProcedure;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@Builder
public class ApplicantsRes {
     private Long recruitId;
     private String  recruitTitle;

     private Long applyId;
     private String applyName;
     private String applyPhone;
     private String applyEmail;
     private ApplyProcedure applyProcedure;
     private Boolean applyDelete;
     private Boolean failApply;
     private Boolean wish;
     private LocalDateTime createdTime;
     private LocalDateTime recentMessageTime;
     private List<String> keywords;

     public static ApplicantsRes fromApply(Apply apply , LocalDateTime recentMessageTime){
          return ApplicantsRes.builder()
                  .recruitId(apply.getRecruit().getRecruitId())
                  .recruitTitle(apply.getRecruit().getRecruitTitle())
                  .applyId(apply.getApplyId())
                  .applyName(apply.getApplyName())
                  .applyPhone(apply.getApplyPhone())
                  .applyEmail(apply.getApplyEmail())
                  .applyProcedure(apply.getApplyProcedure())
                  .applyDelete(apply.isApplyDelete())
                  .failApply(apply.isFailApply())
                  .wish(apply.isWish())
                  .createdTime(apply.getCreatedTime())
                  .recentMessageTime(recentMessageTime)
                  .keywords(doSplit(apply.getKeywordSelect()))
                  .build();
     }

     public static List<String> doSplit(String keywordsSelect){
          if (keywordsSelect != null) {
               final String[] split = keywordsSelect.split(",");
               return Arrays.asList(split);
          } else{
               return new ArrayList<>();
          }
     }
}
