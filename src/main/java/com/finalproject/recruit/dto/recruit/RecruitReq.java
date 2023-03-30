package com.finalproject.recruit.dto.Recruit;

import com.finalproject.recruit.entity.recruit.Recruit;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecruitReq {
    private String title;
    private String contents;
    private String keywordStandard;
    private String resumeTitle;
    //-------------------------------
    private boolean ongoing;
    private String procedure;
    //-------------------------------
    private String docsStart;
    private String docsEnd;
    private String meetStart;
    private String meetEnd;
    private String confirmStart;
    private String confirmEnd;
}
