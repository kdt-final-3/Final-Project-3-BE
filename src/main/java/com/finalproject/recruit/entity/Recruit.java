package com.finalproject.recruit.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

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
    @Column(name = "posting_id")
    private Long recruitId;

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
     * 채용 시작 날짜
     */
    @Column(name = "start_date")
    private LocalDateTime startDate;

    /**
     * 채용 마감 일자
     */
    @Column(name = "end_date")
    private LocalDateTime endDate;

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
     * 기업연결 - 여러 채용공고가 하나의 기업과 연결되어 있을 수 있음
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    /**
     * 기준 키워드
     */
    @Column(name = "keyword_standard")
    private String keywordStandard;




}
