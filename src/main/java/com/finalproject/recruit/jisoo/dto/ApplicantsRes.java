package com.finalproject.recruit.jisoo.dto;

import com.finalproject.recruit.entity.Apply;
import com.finalproject.recruit.jisoo.parameter.Keywords;
import com.finalproject.recruit.parameter.ApplyProcedure;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
     private List<Keywords> keywords;

     public static ApplicantsRes fromApply(Apply apply , LocalDateTime recentMessageTime){
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
                  .keywords(doSplit(apply.getKeywordSelect()))
                  .build();
     }

     public static List<Keywords> doSplit(String keywordsSelect){
          if (keywordsSelect != null) {
               final String[] split = keywordsSelect.split(" ");
               return Arrays.stream(split)
                       .map(Keywords::valueOf)
                       .collect(Collectors.toList());
          } else{
               return new ArrayList<>();
          }
     }
}
