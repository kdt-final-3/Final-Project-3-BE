package com.finalproject.recruit.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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
    private Date startDate;

    /**
     * 채용 마감 일자
     */
    @Column(name = "end_date")
    private Date endDate;

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
     * 템플릿 연결
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_code")
    private Template template;



}
