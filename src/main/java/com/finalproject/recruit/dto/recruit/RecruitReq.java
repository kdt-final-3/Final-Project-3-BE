package com.finalproject.recruit.dto.recruit;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecruitReq {
    private boolean type;
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
