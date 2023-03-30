package com.finalproject.recruit.entity;

import com.finalproject.recruit.dto.recruit.RecruitReq;
import com.finalproject.recruit.entity.Member;
import com.finalproject.recruit.parameter.Procedure;
import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = "recruit_delete_at is NULL")
@Table(name = "recruit")
public class Recruit {

    /*===========================
        채용폼 기본정보
     ===========================*/
    /**
     * 채용폼 ID
     */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recruit_id")
    private Long recruitId;

    /**
     * 채용폼 등록기업
     */
     @ManyToOne(fetch = FetchType.LAZY)
     @JoinColumn(name = "member_email")
     private Member member;


    /*===========================
        채용폼 정보관련
     ===========================*/

    /**
     * 채용폼 제목 ex)2022년 패캠 채용폼
     */
    @Column(name = "recruit_title")
    private String recruitTitle;

    /**
     * 채용폼 내용
     */
    @Column(name = "recruit_content")
    private String recruitContent;

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

    /**
     * 채용링크
     */
    @Column(name = "recruit_url")
    private String recruitUrl;
    /*===========================
        채용폼 상태관련
     ===========================*/
    /**
     * 채용폼 상태 : 진행중 / 진행중이지 않음
     */
    @Column(name = "recruit_ongoing")
    private boolean recruitOngoing;


    /**
     * 채용단계 : 서류 / 면접 / 최종조율 / 대기중 / 마감됨
     */
    @Column(name = "recruit_procedure")
    private String recruitProcedure;

    /**
     * 채용폼 삭제여부
     */
    @Column(name = "recruit_delete")
    private boolean recruitDelete;

    /*===========================
        날짜기록 관련
     ===========================*/
    /**
     * 서류기간 시작
     */
    @Column(name = "start_date")
    private LocalDateTime docsStart;

    /**
     * 서류기간 마감
     */
    @Column(name = "end_date")
    private LocalDateTime docsEnd;

    /**
     * 면접기간 시작
     */
    @Column(name = "meet_start")
    private LocalDateTime meetStart;

    /**
     * 면접기간 마감
     */
    @Column(name = "meet_end")
    private LocalDateTime meetEnd;

    /**
     * 최종조율 시작
     */
    @Column(name = "confirm_start")
    private LocalDateTime confirmStart;

    /**
     * 최종조율 마감 = 채용폼 마감
     */
    @Column(name = "confirm_end")
    private LocalDateTime confirmEnd;

    /*===========================
        유지보수 관련
     ===========================*/
    /**
     * 채용폼 최초등록 시점
     */
    @Column(name = "recruit_registed_at")
    private LocalDateTime recruitRegistedAt;

    // Table Create 시에만 동작
    @PrePersist
    protected void registedAt(){
        recruitRegistedAt = LocalDateTime.now();
    }

    /**
     * 채용폼 변경시점
     */
    @Column(name = "recruit_update_at")
    private Timestamp recruitUpdateAt;


    // Table Update 시에만 동작
    @PreUpdate
    protected void updatedAt(){
        recruitUpdateAt = new Timestamp(new Date().getTime());
    }

    /**
     * 채용폼 삭제요청 시점
     */
    @Column(name = "recruit_delete_at")
    private LocalDateTime recruitDeleteAt;

    /*===========================
        변환 메소드
     ===========================*/
    // 신규 채용폼 등록
    public Recruit(RecruitReq req) {
        this.recruitTitle = req.getTitle();
        this.recruitContent = req.getContents();
        this.keywordStandard = req.getKeywordStandard();
        this.resumeTitle = req.getResumeTitle();
        this.recruitOngoing = req.isOngoing();
        this.recruitProcedure = req.getProcedure();
        this.docsStart = LocalDateTime.parse(req.getDocsStart());
        this.docsEnd = LocalDateTime.parse(req.getDocsEnd());
        this.meetStart = LocalDateTime.parse(req.getMeetStart());
        this.meetEnd = LocalDateTime.parse(req.getMeetEnd());
        this.confirmStart = LocalDateTime.parse(req.getConfirmStart());
        this.confirmEnd = LocalDateTime.parse(req.getConfirmEnd());
    }

    // 기존 채용폼 수정
    public void updateEntity(RecruitReq req){
        this.recruitTitle = (req.getTitle() != null ? req.getTitle() : this.recruitTitle);
        this.recruitContent = (req.getContents() != null ? req.getContents() : this.recruitContent);
        this.keywordStandard = (req.getKeywordStandard() != null ? req.getKeywordStandard() : this.keywordStandard);
        this.resumeTitle = (req.getResumeTitle() != null ? req.getResumeTitle() : this.recruitTitle);
        this.recruitOngoing = (req.isOngoing() != this.recruitOngoing ? req.isOngoing() : this.recruitOngoing);
        this.recruitProcedure = (req.getProcedure() != null ? req.getProcedure() : this.recruitProcedure);
        this.docsStart = (req.getDocsStart() != null ? LocalDateTime.parse(req.getDocsStart()) : this.docsStart);
        this.docsEnd = (req.getDocsEnd() != null ? LocalDateTime.parse(req.getDocsEnd()) : this.docsEnd);
        this.meetStart = (req.getMeetStart() != null ? LocalDateTime.parse(req.getMeetStart()) : this.meetStart);
        this.meetEnd = (req.getMeetEnd() != null ? LocalDateTime.parse(req.getMeetEnd()) : this.meetEnd);
        this.confirmStart = (req.getConfirmStart() != null ? LocalDateTime.parse(req.getConfirmStart()) : this.confirmStart);
        this.confirmEnd = (req.getConfirmEnd() != null ? LocalDateTime.parse(req.getConfirmEnd()) : this.confirmEnd);
    }

    // 채용폼 URL 등록
    public void setRecruitUrl(String url){
        this.recruitUrl = url;
    }

    // 채용폼 삭제
    public void setRecruitDelete(){
        this.recruitDeleteAt = LocalDateTime.now();
        this.recruitDelete = true;
    }

    // 현재날짜 기준, 기간별 채용폼 상태설정
    public void adjustProcedure(){
        if(this.recruitOngoing){
            LocalDateTime current = LocalDateTime.now();

            String procedure = (
                    current.isAfter(this.getDocsStart()) && current.isBefore(this.getDocsEnd()) ? Procedure.DOCS.name()
                            : current.isAfter(this.getMeetStart()) && (current.isBefore(this.getMeetEnd())) ? Procedure.MEET.name()
                            : current.isAfter(this.getConfirmStart()) && (current.isBefore(this.getConfirmEnd())) ? Procedure.CONFIRM.name()
                            : Procedure.ONHOLD.name()
            );

            this.recruitProcedure = procedure;
        }else {
            this.recruitProcedure = Procedure.CLOSED.name();
        }
    }
}