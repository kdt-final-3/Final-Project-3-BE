package com.finalproject.recruit.controller;

import com.finalproject.recruit.dto.notice.EmailReq;
import com.finalproject.recruit.dto.notice.SelectStepReq;
import com.finalproject.recruit.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> recruitList(){
        String email = "a@naver.com";
        return noticeService.recruitList(email);
    }

    /**
     * 특정 채용폼의 지원자 목록 조회
     * */
    @GetMapping("/{recruitId}")
    public ResponseEntity<?> applicantList(@PathVariable Long recruitId){
        return noticeService.applicantList(recruitId);
    }


    /**
     * 이메일 보내기 (일단 1인)
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
        String email = "a@naver.com";
        return noticeService.messageHistory(recruitId);
    }


    /**
     * 이메일 보내기 전 절차 선택
     * */
    @PostMapping("/select")
    public ResponseEntity<?> selectStep(@RequestBody SelectStepReq selectStepReq){
        return noticeService.selectStep(selectStepReq);
    }

}
