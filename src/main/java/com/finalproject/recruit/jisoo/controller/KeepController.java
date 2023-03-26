package com.finalproject.recruit.jisoo.controller;

import com.finalproject.recruit.jisoo.dto.ApplicantsRes;
import com.finalproject.recruit.jisoo.dto.RecentRecruitRes;
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


    @GetMapping("/home")
    public RecentRecruitRes recentRecruit(){
        return keepService.recentRecruit();
    }

    @GetMapping("/view-applicant/{recruitId}")
    public List<ApplicantsRes> dropApplicants (@PathVariable Long recruitId){
        return keepService.dropApplicants(recruitId);
    }

}
