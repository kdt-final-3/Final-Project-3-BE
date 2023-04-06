package com.finalproject.recruit.dto.notice;

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
        private LocalDateTime recruitRegisteredAt;

        public static NoticeRecruitsRes fromRecruit(Recruit recruit){
            return NoticeRecruitsRes.builder()
                    .recruitId(recruit.getRecruitId())
                    .recruitTitle(recruit.getRecruitTitle())
                    .recruitProcedure(recruit.getRecruitProcedure())
                    .recruitRegisteredAt(recruit.getRecruitRegistedAt())
                    .build();

        }

}
