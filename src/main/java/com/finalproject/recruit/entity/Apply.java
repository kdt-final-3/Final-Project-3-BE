package com.finalproject.recruit.entity;

import com.finalproject.recruit.entity.apply.*;
import com.finalproject.recruit.parameter.MilitaryEnum;
import com.finalproject.recruit.parameter.ApplyProcedure;
import com.finalproject.recruit.util.BaseTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "apply")
public class Apply extends BaseTime {

    /**
     * 지원서 고유값
     */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
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
     * 기타 이력서
     */
    @Column(name = "apply_resume")
    private String applyResume;

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
    @Column(name = "wish")
    private boolean wish;

    private LocalDateTime checkApply;

    private LocalDateTime meeting;

    private LocalDateTime passDay;

    @Column(name = "fail_apply")
    private boolean failApply;

    /**
     * 장애여부
     * */
    private boolean disorder;
    /**
     * 보훈
     * */
    private boolean veteran;

    /**
     * 고용지원금
     * */
    private boolean employment;

    /**
     * 병역
     */
    private MilitaryEnum militaryEnum;

    /**
     * 약관
     * */
    private boolean terms;

    /**
     * 경력
     */
    @OneToOne(mappedBy = "apply")
    private Career career;

    public void setPaperSubmit(){
        this.applyProcedure = ApplyProcedure.서류제출;
    }

    /**
     * 학력
     */
    @OneToOne(mappedBy = "apply")
    private Education education;

    /**
     * 어학
     */
    @OneToOne(mappedBy = "apply")
    private Language language;

    /**
     * 병역사항
     */
    @OneToOne(mappedBy = "apply")
    private Military military;

    /**
     * 자격증
     */
    @OneToOne(mappedBy = "apply")
    private Certificate certificate;

    /**
     * 대외활동
     */
    @OneToOne(mappedBy = "apply")
    private Activities activities;

    /**
     * 수상
     */
    @OneToOne(mappedBy = "apply")
    private Awards awards;

    /**
     * 채용단계 변경
     * @param applyProcedure
     */
    public void changeProcedure(ApplyProcedure applyProcedure) {
        this.applyProcedure = applyProcedure;
    }

    /**
     * 합격 처리
     */
    public void changePass() {
        this.pass = true;
        this.passDay = LocalDateTime.now();
        this.failApply = false;
    }

    /**
     * 불합격 처리
     */
    public void cancelPass() {
        this.pass = false;
        this.failApply = true;
    }

    /**
     * 코멘트 등록
     */
    public void writeEvaluation(String evaluation) {
        this.evaluation = evaluation;
    }

    /**
     * 찜 처리
     */
    public void changeWish() {
        this.wish = true;
    }

    /**
     * 찜 해제
     */
    public void cancelWish() {
        this.wish = false;
    }

    /**
     * 탈락인재 처리
     */
    public void changeDrop() {
        this.failApply = true;
    }

    /**
     * 탈락인재 해제
     */
    public void cancelDrop() {
        this.failApply = false;
    }

    /**
     * 면접일 지정
     */
    public void setMeeting(LocalDateTime meeting) {
        this.meeting = meeting;
    }

    /**
     * 서류검토 처리
     */
    public void setCheckApply() {
        this.checkApply = LocalDateTime.now();
    }

}
