package com.finalproject.recruit.entity;

import com.finalproject.recruit.util.BaseTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "apply")
public class Apply extends BaseTime {

    /**
     * 지원서 고유값
     */
    @Id
    @Column(name = "apply_id")
    private Long applyId;

    /**
     * 채용공고 연결
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "posting_id")
    private Recruit recruit;

    /**
     * 지원자 이름
     */
    @Column(name = "apply_name")
    private String applyName;

    /**
     * 지원자 전화번호
     */
    @Column(name = "apply_phone")
    private String applyPhone;

    /**
     * 지원자 이메일
     */
    @Column(name = "apply_email")
    private String applyEmail;

    /**
     * 학력
     */
    @Column(name = "apply_education")
    private String applyEducation;

    /**
     * 경력사항
     */
    @Column(name = "apply_career")
    private String applyCareer;

    /**
     * 대외활동
     */
    @Column(name = "apply_activities")
    private String applyActivities;

    /**
     * 자격증
     */
    @Column(name = "apply_certificate")
    private String applyCertificate;

    /**
     * 교육이수
     */
    @Column(name = "apply_curriculum")
    private String applyCurriculum;

    /**
     * 수상내역
     */
    @Column(name = "apply_awards")
    private String applyAwards;

    /**
     * 해외경험
     */
    @Column(name = "apply_overseas")
    private String applyOverseas;

    /**
     * 어학능력
     */
    @Column(name = "apply_language")
    private String applyLanguage;

    /**
     * 포트폴리오
     */
    @Column(name = "apply_portfolio")
    private String applyPortfolio;

    /**
     * 병역
     */
    @Column(name = "apply_military")
    private String applyMilitary;

    /**
     * 자기소개서
     */
    @Column(name = "apply_resume")
    private String applyResume;

    /**
     * 채용 절차
     */
    @Column(name = "apply_procedure")
    private String applyProcedure;

    /**
     * 평가
     */
    @Column(name = "evaluation")
    private String evaluation;

    /**
     * 선호
     */
    @Column(name = "prefer")
    private boolean prefer;

    /**
     * 합/불
     */
    @Column(name = "pass")
    private boolean pass;

    /**
     * 삭제여부
     */
    @Column(name = "apply_delete")
    private boolean applyDelete;



}
