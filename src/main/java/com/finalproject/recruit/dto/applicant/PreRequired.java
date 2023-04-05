package com.finalproject.recruit.dto.applicant;

import com.finalproject.recruit.entity.Recruit;
import com.finalproject.recruit.parameter.Keywords;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class PreRequired {

    private Long recruitId;
    private String recruitTitle;
    private String recruitContent;
    private String resumeTitle;
    private List<Keywords> keywords;
    private LocalDateTime docsStart;
    private LocalDateTime docsEnd;
    private LocalDateTime meetStart;
    private LocalDateTime meetEnd;
    private LocalDateTime confirmStart;
    private LocalDateTime confirmEnd;

    public static PreRequired fromRecruit(Recruit recruit) {
        return PreRequired.builder()
                .recruitId(recruit.getRecruitId())
                .recruitTitle(recruit.getRecruitTitle())
                .recruitContent(recruit.getRecruitContent())
                .resumeTitle(recruit.getResumeTitle())
                .keywords(List.of(Keywords.values()))
                .docsStart(recruit.getDocsStart())
                .docsEnd(recruit.getDocsEnd())
                .meetStart(recruit.getMeetStart())
                .meetEnd(recruit.getMeetEnd())
                .confirmStart(recruit.getConfirmStart())
                .confirmEnd(recruit.getConfirmEnd())
                .build();
    }
}
