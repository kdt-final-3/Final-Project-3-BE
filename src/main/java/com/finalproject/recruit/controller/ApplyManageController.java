package com.finalproject.recruit.controller;

import com.finalproject.recruit.service.ApplyManageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ApplyManageController {

    private final ApplyManageService applyManageService;

    @GetMapping("/manage/{recruitId}")
    public ResponseEntity<?> findAllApplicants(@PathVariable Long recruitId) {

        return applyManageService.findAllApplicants(recruitId);
    }
}
