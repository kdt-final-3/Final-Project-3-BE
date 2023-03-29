package com.finalproject.recruit.jisoo.controller;

import com.finalproject.recruit.dto.Response;
import com.finalproject.recruit.jisoo.dto.ApplicantInfoReq;
import com.finalproject.recruit.jisoo.dto.ApplicationReq;
import com.finalproject.recruit.jisoo.service.ApplicantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/view")
@RequiredArgsConstructor
public class ApplicantController {

    private final ApplicantService applicantService;

    private final Response response;



    @PostMapping("/submit/info")
    public Long postInfo(@RequestBody ApplicantInfoReq applicantInfoReq){
        return applicantService.postInfo(applicantInfoReq);
    }

    /**
     * 지원자의 지원서 제출
     * */
    @PostMapping("/submit")
    public ResponseEntity<?> postApplication (@RequestBody ApplicationReq applicationReq){
        return applicantService.postApplication(applicationReq);
    }

    /**
     * 지원자 중복 지원 체크
     * */
    @GetMapping("/check/{recruitId}")
    public ResponseEntity<?> checkEmail(@RequestParam String email, @PathVariable Long recruitId){
        return response.success(applicantService.checkEmail(email, recruitId));
    }

}
