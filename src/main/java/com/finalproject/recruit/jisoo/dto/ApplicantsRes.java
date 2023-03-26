package com.finalproject.recruit.jisoo.dto;

import com.finalproject.recruit.entity.Apply;
import com.finalproject.recruit.parameter.ApplyProcedure;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ApplicantsRes {
     private String applyName;
     private String applyPhone;
     private String applyEmail;
     private ApplyProcedure applyProcedure;
     private Boolean applyDelete;
     private Boolean pass;
     private Boolean wish;
     private LocalDateTime createdTime;
     private LocalDateTime recentMessageTime;
     private String keywordsSelect; //배열로

     public static ApplicantsRes fromApply(Apply apply , LocalDateTime recentMessageTime, String keywords){
          return ApplicantsRes.builder()
                  .applyName(apply.getApplyName())
                  .applyPhone(apply.getApplyPhone())
                  .applyEmail(apply.getApplyEmail())
                  .applyProcedure(apply.getApplyProcedure())
                  .applyDelete(apply.isApplyDelete())
                  .pass(apply.isPass())
                  .wish(apply.isWish())
                  .createdTime(apply.getCreatedTime())
                  .recentMessageTime(recentMessageTime)
                  .keywordsSelect(keywords)
                  .build();
     }
}
