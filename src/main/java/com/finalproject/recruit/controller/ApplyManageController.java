package com.finalproject.recruit.controller;

import com.finalproject.recruit.dto.applymanage.ApplyIdsReq;
import com.finalproject.recruit.dto.applymanage.ApplyProcedureReq;
import com.finalproject.recruit.dto.applymanage.EvaluationReq;
import com.finalproject.recruit.dto.applymanage.MeetingDateReq;
import com.finalproject.recruit.service.ApplyManageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ApplyManageController {

    private final ApplyManageService applyManageService;

    /**
     * 전체 지원자 조회
     */
    @GetMapping("/manage/{recruitId}")
    public ResponseEntity<?> findAllApplicants(@PathVariable Long recruitId) {

        return applyManageService.findAllApplicants(recruitId);
    }

    /**
     * 채용단계에 맞는 지원자 조회
     */
    @GetMapping("/manage/{recruitId}/{procedure}")
    public ResponseEntity<?> findApplicantByProcedure(@PathVariable Long recruitId,
                                                      @PathVariable String procedure) {

        return applyManageService.findApplicantByProcedure(recruitId, procedure);
    }

    /**
     * 지원자 채용단계 변경
     */
    @PutMapping("/manage/change_procedure")
    public ResponseEntity<?> changeApplyProcedure(@RequestBody ApplyProcedureReq applyProcedureReq) {

        return applyManageService.changeApplyProcedure(applyProcedureReq);
    }

    /**
     * 지원자 합격 / 불합격 처리
     */
    @PutMapping("/manage/pass/{applyId}")
    public ResponseEntity<?> changePassOrFail(@PathVariable Long applyId) {

        return applyManageService.changePassOrFail(applyId);
    }

    /**
     * 총 지원자 수, 오늘 지원자 수, 채용공고의 채용단계, 채용단계 마감 D-day
     */
    @GetMapping("/manage/status/{recruitId}")
    public ResponseEntity<?> countApplicantAndProcessAndTime(@PathVariable Long recruitId) {

        return applyManageService.countApplicantAndProcessAndTime(recruitId);
    }

    /**
     * 지원자 상세조회
     */
    @GetMapping("/apply/{applyId}")
    public ResponseEntity<?> findApplicantDetail(@PathVariable Long applyId) {

        return applyManageService.findApplicantDetail(applyId);
    }

    /**
     * 지원자 코멘트 등록
     */
    @PutMapping("/apply/note/{applyId}")
    public ResponseEntity<?> writeEvaluation(@PathVariable Long applyId,
                                             @RequestBody EvaluationReq evaluationReq) {

        return applyManageService.writeEvaluation(applyId, evaluationReq);
    }

    /**
     * 지원자 찜 등록 / 해제
     */
    @PutMapping("/apply/wish/{applyId}")
    public ResponseEntity<?> changeWish(@PathVariable Long applyId) {

        return applyManageService.changeWish(applyId);
    }

    /**
     * 지원자 탈락인재 보관함 등록 / 해제
     */
    @PutMapping("/apply/drop/{applyId}")
    public ResponseEntity<?> dropApply(@PathVariable Long applyId) {

        return applyManageService.dropApply(applyId);
    }

    /**
     * 지원자 탈락인재 보관함 일괄 등록 (선택된 지원자만)
     */
    @PutMapping("/apply/drop")
    public ResponseEntity<?> dropApplicants(@RequestBody ApplyIdsReq applyIdsReq) {

        return applyManageService.dropApplicants(applyIdsReq);
    }

    /**
     * 지원자 면접날짜 지정
     */
    @PutMapping("/apply/set_meeting/{applyId}")
    public ResponseEntity<?> setMeetDay(@PathVariable Long applyId,
                                        @RequestBody MeetingDateReq meetingDateReq) {

        return applyManageService.setMeetDay(applyId, meetingDateReq);
    }

    /**
     * 지원자 서류검토 처리
     */
    @PutMapping("/apply/check/{applyId}")
    public ResponseEntity<?> checkApplicant(@PathVariable Long applyId) {

        return applyManageService.checkApplicant(applyId);
    }
}
