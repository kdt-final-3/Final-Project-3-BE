package com.finalproject.recruit.controller;

import com.finalproject.recruit.service.ApplyManageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @PutMapping("/manage/change/{applyId}/{procedure}")
    public ResponseEntity<?> changeApplyProcedure(@PathVariable Long applyId,
                                                  @PathVariable String procedure) {

        return applyManageService.changeApplyProcedure(applyId, procedure);
    }
}
