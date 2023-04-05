package com.finalproject.recruit.dto.notice;

import com.finalproject.recruit.parameter.NoticeStep;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class EmailReq {
    private Long recruitId;
    private List<Long> applyIds;
    private String mailContent;
    private NoticeStep noticeStep;
    private LocalDateTime interviewDate;
}
