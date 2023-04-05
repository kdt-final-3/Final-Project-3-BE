package com.finalproject.recruit.controller;

import com.finalproject.recruit.dto.member.AuthDTO;
import com.finalproject.recruit.dto.notice.EmailReq;
import com.finalproject.recruit.dto.notice.SelectStepReq;
import com.finalproject.recruit.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/notice")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    /**
     * 기업의 채용폼 목록 조회
     * */
    @GetMapping
    public ResponseEntity<?> recruitList(Authentication authentication){
        AuthDTO memberInfo = (AuthDTO) authentication.getPrincipal();
        return noticeService.recruitList(memberInfo.getMemberEmail());
    }

    /**
     * 특정 채용폼의 지원자 목록 조회
     * */
    @GetMapping("/{recruitId}")
    public ResponseEntity<?> applicantList(@PathVariable Long recruitId, Pageable pageable){
        return noticeService.applicantList(recruitId, pageable);
    }


    /**
     * 단체 이메일 보내기
     * */
    @PostMapping("/send")
    public ResponseEntity<?> sendEmail(@RequestBody EmailReq emailReq){
        return noticeService.sendEmail(emailReq);
    }


    /**
     * 기업의 이메일 전송 내역 확인 -> 채용공고 별 이메일 전송 내역 확인
     * */
    @GetMapping("/status/{recruitId}")
    public ResponseEntity<?> messageHistory(@PathVariable Long recruitId){
        return noticeService.messageHistory(recruitId);
    }


    /**
     * 이메일 보내기 전 절차 선택
     * */
    @PostMapping("/select")
    public ResponseEntity<?> selectStep(@RequestBody SelectStepReq selectStepReq){
        return noticeService.selectStep(selectStepReq);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchApplicant(@RequestParam String applyName, Long recruitId){
        return noticeService.searchApplicant(applyName, recruitId);
    }
}
