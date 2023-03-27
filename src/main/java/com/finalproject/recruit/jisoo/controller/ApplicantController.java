package com.finalproject.recruit.jisoo.controller;

import com.finalproject.recruit.jisoo.dto.ApplicationReq;
import com.finalproject.recruit.jisoo.service.ApplicantService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController()
@RequestMapping("/view")
@RequiredArgsConstructor
public class ApplicantController {

    private final ApplicantService applicantService;

    @PostMapping("/submit")
    public String postApplication (@RequestBody ApplicationReq applicationReq){
        System.out.println(LocalDateTime.now());
        return applicantService.postApplication(applicationReq);
    }

}
