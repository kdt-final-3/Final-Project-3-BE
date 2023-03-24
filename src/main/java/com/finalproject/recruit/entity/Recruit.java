package com.finalproject.recruit.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "recruit")
public class Recruit {

    /**
     * 채용공고 ID
     */
    @Id
    @Column(name = "recruit_id")
    private Long recruitId;

    /**
     * 기업연결 - 여러 채용공고가 하나의 기업과 연결되어 있을 수 있음
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    /**
     * 채용공고 명 ex)2022년 패캠 채용공고
     */
    @Column(name = "recruit_title")
    private String recruitTitle;

    /**
     * 채용공고 내용
     */
    @Column(name = "recruit_content")
    private String recruitContent;

    /**
     * 서류 시작 날짜
     */
    @Column(name = "start_date")
    private LocalDateTime startDate;

    /**
     * 서류마감 일자
     */
    @Column(name = "end_date")
    private LocalDateTime endDate;

    /**
     * 면접 시작 날짜
     */
    @Column(name = "meet_start")
    private LocalDateTime meetStart;

    /**
     * 면접 종료 날짜
     */
    @Column(name = "meet_end")
    private LocalDateTime meetEnd;

    /**
     * 최종발표
     */
    @Column(name = "goal")
    private LocalDateTime goal;

    /**
     * 삭제 여부
     */
    @Column(name = "recruit_delete")
    private boolean recruitDelete;

    /**
     * 채용단계
     */
    @Column(name = "recruit_procedure")
    private String recruitProcedure;

    /**
     * 기준 키워드
     */
    @Column(name = "keyword_standard")
    private String keywordStandard;

    /**
     * 자개소개서 주제
     */
    @Column(name = "resume_title")
    private String resumeTitle;



}
