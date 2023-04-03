package com.finalproject.recruit.controller;

import com.finalproject.recruit.service.KeepService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
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
    public ResponseEntity<?> recentRecruit(Pageable pageable){
        String email = "a@naver.com";
        return keepService.recentRecruit(email, pageable);
    }

    /**
     * 탈락 인재 조회
     * */
    @GetMapping("/view-applicant/{recruitId}")
    public ResponseEntity<?> dropApplicants (@PathVariable Long recruitId, Pageable pageable){
        return keepService.dropApplicants(recruitId, pageable);
    }


    /**
     * 영구 삭제 지원자 조회
     * */
    @GetMapping("/view-applicant/eternal/{recruitId}")
    public ResponseEntity<?> eternalDeleteApplicants(@PathVariable Long recruitId, Pageable pageable){
        return keepService.eternalDeleteApplicants(recruitId, pageable);
    }

}
