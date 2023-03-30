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

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private LocalDateTime meetStart;

    private LocalDateTime meetEnd;

    private LocalDateTime goal;

    private String resumeTitle;

    private List<Keywords> keywords;

    public static PreRequired fromRecruit(Recruit recruit) {
        return PreRequired.builder()
                .recruitId(recruit.getRecruitId())
                .recruitTitle(recruit.getRecruitTitle())
                .recruitContent(recruit.getRecruitContent())
                .startDate(recruit.getStartDate())
                .endDate(recruit.getEndDate())
                .meetStart(recruit.getMeetStart())
                .meetEnd(recruit.getMeetEnd())
                .goal(recruit.getGoal())
                .resumeTitle(recruit.getResumeTitle())
                .keywords(List.of(Keywords.values()))
                .build();
    }

}
