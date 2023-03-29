package com.finalproject.recruit.jisoo.dto;

import com.finalproject.recruit.jisoo.parameter.NoticeStep;
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
