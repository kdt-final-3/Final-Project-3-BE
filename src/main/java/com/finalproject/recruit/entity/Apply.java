package com.finalproject.recruit.entity;

import com.finalproject.recruit.parameter.ApplyProcedure;
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
     * 선택 키워드
     */
    @Column(name = "keyword_select")
    private String keywordSelect;

    /**
     * 채용공고 연결
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruit_id")
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
     * 포트폴리오
     */
    @Column(name = "apply_portfolio")
    private String applyPortfolio;

    /**
     * 채용 절차
     */
    @Column(name = "apply_procedure")
    @Enumerated(EnumType.STRING)
    private ApplyProcedure applyProcedure;

    /**
     * 평가
     */
    @Column(name = "evaluation")
    private String evaluation;

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

    /**
     * 자기소개서 내용
     */
    @Column(name = "resume_content")
    private String resumeContent;

    /**
     * 찜
     */
    @Column(name = "like")
    private boolean like;

}
