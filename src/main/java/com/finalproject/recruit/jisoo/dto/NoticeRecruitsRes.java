package com.finalproject.recruit.jisoo.dto;

import com.finalproject.recruit.entity.Recruit;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class NoticeRecruitsRes {
        private Long recruitId;
        private String recruitTitle;
        private String recruitProcedure;
        private Boolean recruitClose;
        private LocalDateTime createdTime;

        public static NoticeRecruitsRes fromRecruit(Recruit recruit){
            return NoticeRecruitsRes.builder()
                    .recruitId(recruit.getRecruitId())
                    .recruitTitle(recruit.getRecruitTitle())
                    .recruitProcedure(recruit.getRecruitProcedure())
                    .recruitClose(recruit.isRecruitClose())
                    .createdTime(recruit.getCreatedTime())
                    .build();

        }

}
