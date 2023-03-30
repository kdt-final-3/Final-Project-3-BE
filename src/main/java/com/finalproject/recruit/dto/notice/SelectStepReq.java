package com.finalproject.recruit.dto.notice;

import com.finalproject.recruit.parameter.NoticeStep;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SelectStepReq {

    private Long recruitId;
    private NoticeStep noticeStep;

}
