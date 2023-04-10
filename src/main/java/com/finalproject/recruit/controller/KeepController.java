package com.finalproject.recruit.controller;

import com.finalproject.recruit.dto.applymanage.ApplyIdsReq;
import com.finalproject.recruit.dto.member.AuthDTO;
import com.finalproject.recruit.service.KeepService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
    public ResponseEntity<?> recentRecruit(Authentication authentication , Pageable pageable){
        AuthDTO memberInfo = (AuthDTO) authentication.getPrincipal();
        return keepService.recentRecruit(memberInfo.getMemberEmail(), pageable);
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

    /**
     * 지원자 탈락인재 보관함 일괄 해제 (선택된 지원자만)
     */
    @PutMapping("/cancel")
    public ResponseEntity<?> dropCancelApplicants(@RequestBody ApplyIdsReq applyIdsReq) {

        return keepService.dropCancelApplicants(applyIdsReq);
    }

    @GetMapping("/view-applicant/search")
    public ResponseEntity<?> searchDropApplicants(@RequestParam String applyName, Long recruitId, Pageable pageable){
        return keepService.searchDropApplicants(recruitId, applyName, pageable);
    }

}
