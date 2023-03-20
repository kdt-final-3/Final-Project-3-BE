package com.finalproject.recruit.entity;

import com.finalproject.recruit.util.BaseTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "template")
public class Template extends BaseTime {

    /**
     * 템플릿 고유값
     */
    @Id
    @Column(name = "template_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long templateId;

    /**
     * 템플릿 이름
     */
    @Column(name = "template_title")
    private boolean templateTitle;

    /**
     * 학력
     */
    @Column(name = "template_education")
    private boolean templateEducation;


    /**
     * 경력사항
     */
    @Column(name = "template_career")
    private boolean templateCareer;

    /**
     * 대외활동
     */
    @Column(name = "template_activities")
    private boolean templateActivities;

    /**
     * 자격증
     */
    @Column(name = "template_certificate")
    private boolean templateCertificate;

    /**
     * 교육이수
     */
    @Column(name = "template_curriculum")
    private boolean templateCurriculum;

    /**
     * 수상
     */
    @Column(name = "template_awards")
    private boolean template_awards;

    /**
     * 해외경험
     */
    @Column(name = "template_overseas")
    private boolean templateOverseas;

    /**
     * 어학
     */
    @Column(name = "template_language")
    private boolean templateLanguage;

    /**
     * 포트폴리오
     */
    @Column(name = "template_portfolio")
    private boolean templatePortfolio;

    /**
     * 병역사항
     */
    @Column(name = "template_military")
    private boolean templateMilitary;

    /**
     * 자기소개서
     */
    @Column(name = "template_resume")
    private boolean templateResume;


    /**
     * 기업연결 - 여러 템플릿이 하나의 기업과 연결되어 있을 수 있음
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
}
