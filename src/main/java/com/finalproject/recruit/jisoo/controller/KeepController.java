package com.finalproject.recruit.jisoo.controller;

import com.finalproject.recruit.jisoo.dto.ApplicantsRes;
import com.finalproject.recruit.jisoo.service.KeepService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
@RequestMapping("/drop")
@RequiredArgsConstructor
public class KeepController {

    private final KeepService keepService;


    /**
     * 최근 등록 채용폼의 탈락 인재 조회
     * */
    @GetMapping("/home")
    public List<ApplicantsRes> recentRecruit(){
        return keepService.recentRecruit();
    }

    /**
     * 탈락 인재 조회
     * */
    @GetMapping("/view-applicant/{recruitId}")
    public List<ApplicantsRes> dropApplicants (@PathVariable Long recruitId){
        return keepService.dropApplicants(recruitId);
    }



    /**
     * 영구 삭제 지원자 조회
     * */
    @GetMapping("/view-applicant/eternal/{recruitId}")
    public List<ApplicantsRes> eternalDeleteApplicants(@PathVariable Long recruitId){
        return keepService.eternalDeleteApplicants(recruitId);
    }

}
