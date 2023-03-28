package com.finalproject.recruit.jisoo.dto;

import com.finalproject.recruit.entity.Apply;
import com.finalproject.recruit.jisoo.Mail;
import com.finalproject.recruit.jisoo.parameter.NoticeStep;
import com.finalproject.recruit.parameter.ApplyProcedure;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class MessageHistoryRes {

     private Long mailId;
     private String mailContent;
     private NoticeStep noticeStep;
     private LocalDateTime createdTime;

     private String applyName;
     private String applyPhone;
     private String applyEmail;
     private ApplyProcedure applyProcedure;

     public static MessageHistoryRes fromEntity(Mail mail, Apply apply){
          return MessageHistoryRes.builder()
                  .mailId(mail.getMailId())
                  .mailContent(mail.getMailContent())
                  .noticeStep(mail.getNoticeStep())
                  .createdTime(mail.getCreatedTime())
                  .applyName(apply.getApplyName())
                  .applyPhone(apply.getApplyPhone())
                  .applyEmail(apply.getApplyEmail())
                  .applyProcedure(apply.getApplyProcedure())
                  .build();
     }
}
