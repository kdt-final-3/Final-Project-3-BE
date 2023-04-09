package com.finalproject.recruit.dto.applymanage;

import com.finalproject.recruit.entity.Apply;
import com.finalproject.recruit.parameter.ApplyProcedure;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApplyResponseDTO {

    Long applyId;
    String applyName;
    String applyPhone;
    String applyEmail;
    ApplyProcedure applyProcedure;
    Boolean pass;
    Boolean failApply;
    LocalDateTime createdTime;
    Boolean applyDelete;
    Boolean wish;
    String keywords;
    int score;
    List<String> keywordList;

    public ApplyResponseDTO(Apply apply) {
        this.applyId = apply.getApplyId();
        this.applyName = apply.getApplyName();
        this.applyPhone = apply.getApplyPhone();
        this.applyEmail = apply.getApplyEmail();
        this.applyProcedure = apply.getApplyProcedure();
        this.pass = apply.isPass();
        this.failApply = apply.isFailApply();
        this.createdTime = apply.getCreatedTime();
        this.applyDelete = apply.isApplyDelete();
        this.wish = apply.isWish();
        this.keywords = apply.getKeywordSelect();
    }
}
