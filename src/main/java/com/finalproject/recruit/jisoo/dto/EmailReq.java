package com.finalproject.recruit.jisoo.dto;

import com.finalproject.recruit.jisoo.parameter.NoticeStep;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class EmailReq {
    private Long recruitId;
    private Long applyId;
    private String mailContent;
    private NoticeStep noticeStep;
    private LocalDateTime interviewDate;
}
