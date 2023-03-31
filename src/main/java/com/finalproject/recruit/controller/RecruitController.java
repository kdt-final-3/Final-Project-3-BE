package com.finalproject.recruit.controller;

import com.finalproject.recruit.dto.Response;
import com.finalproject.recruit.dto.ResponseDTO;
import com.finalproject.recruit.dto.recruit.RecruitRes;
import com.finalproject.recruit.service.RecruitService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/recruit")
@RequiredArgsConstructor
public class RecruitController {
    private final RecruitService recruitService;

    /*===========================
        채용폼 목록조회
     ===========================*/
    // 채용상태 : recruit_status
    // ( true : 진행중 / false : 마감됨  )
    @GetMapping("/")
    public ResponseDTO<List<RecruitRes>> selectAllRecruit(@RequestParam(name = "status") String recruitStatus){
        String memberId = null;
        List<RecruitRes> res = recruitService.selectALlRecruit(memberId, Boolean.parseBoolean(recruitStatus));
        return ResponseDTO.message(res);
    }
}
