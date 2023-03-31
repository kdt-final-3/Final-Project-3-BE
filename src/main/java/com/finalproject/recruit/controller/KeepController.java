package com.finalproject.recruit.controller;

import com.finalproject.recruit.service.KeepService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/drop")
@RequiredArgsConstructor
public class KeepController {

    private final KeepService keepService;


    /**
     * 최근 등록 채용폼의 탈락 인재 조회
     * */
    @GetMapping("/home")
    public ResponseEntity<?> recentRecruit(){
        String email = "a@naver.com";
        return keepService.recentRecruit(email);
    }

    /**
     * 탈락 인재 조회
     * */
    @GetMapping("/view-applicant")
    public ResponseEntity<?> dropApplicants (@RequestParam Long recruitId){
        return keepService.dropApplicants(recruitId);
    }


    /**
     * 영구 삭제 지원자 조회
     * */
    @GetMapping("/view-applicant/eternal/{recruitId}")
    public ResponseEntity<?> eternalDeleteApplicants(@PathVariable Long recruitId){
        return keepService.eternalDeleteApplicants(recruitId);
    }

}
