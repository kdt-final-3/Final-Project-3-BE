package com.finalproject.recruit.controller;

import com.finalproject.recruit.dto.Response;
import com.finalproject.recruit.dto.member.AuthDTO;
import com.finalproject.recruit.dto.recruit.RecruitReq;
import com.finalproject.recruit.dto.recruit.RecruitRes;
import com.finalproject.recruit.service.RecruitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/recruit")
@RequiredArgsConstructor
public class RecruitController {
    private final RecruitService recruitService;

    private final Response response;

    /*===========================
        채용폼 목록조회
     ===========================*/
    // 채용상태 : recruit_status
    // ( true : 진행중 / false : 마감됨  )
    @GetMapping
    public ResponseEntity<?> selectAllRecruit(@RequestParam(name = "status") String recruitStatus,
                                           Authentication authentication){
        AuthDTO memberInfo = (AuthDTO) authentication.getPrincipal();
        return recruitService.selectALlRecruit(
                memberInfo.getMemberEmail(), Boolean.parseBoolean(recruitStatus));
    }

    /*===========================
        채용폼 검색
    ===========================*/
    // 채용상태 : recruit_status
    // ( true : 진행중 / false : 마감됨  )
    @GetMapping("/search")
    public ResponseEntity<?> searchRecruit(@RequestParam(name = "status") String recruitStatus,
                                                       @RequestParam(name = "title") String recruitTitle,
                                                       Authentication authentication){
        AuthDTO memberInfo = (AuthDTO) authentication.getPrincipal();
        return recruitService.searchRecruit(memberInfo.getMemberEmail(), Boolean.parseBoolean(recruitStatus), recruitTitle);
    }

    /*===========================
        채용폼 상세조회
    ===========================*/
    @GetMapping("/{recruit_id}")
    public ResponseEntity<?> detailRecruit(@PathVariable Long recruit_id,
                                           Authentication authentication){
        AuthDTO memberInfo = (AuthDTO) authentication.getPrincipal();
        return recruitService.selectRecruitDetail(memberInfo.getMemberEmail(), recruit_id);
    }

    /*===========================
      채용폼 상세조회 캐싱 추가
  ===========================*/
    @GetMapping("/cache/{recruit_id}")
    public ResponseEntity<?> detailRecruitCache(@PathVariable Long recruit_id,
                                           Authentication authentication){
        AuthDTO memberInfo = (AuthDTO) authentication.getPrincipal();
        RecruitRes recruitRes = recruitService.selectRecruitDetailCache(memberInfo.getMemberEmail(), recruit_id);
        return response.success(recruitRes);
    }

    /*===========================
        채용폼 상세조회 Redis
    ===========================*/
    @GetMapping("/recent/{memberEmail}")
    public ResponseEntity<?> findRecentRecruit(@PathVariable String memberEmail){
        return recruitService.findRecentRecruit(memberEmail);
    }


    /*===========================
        채용폼 수정
    ===========================*/
    @PutMapping("/{recruit_id}")
    public ResponseEntity<?> editRecruit(@RequestBody RecruitReq req, @PathVariable Long recruit_id){
        return recruitService.editRecruit(req, recruit_id);
    }

    /*===========================
      채용폼 수정 : 캐싱 사용
  ===========================*/
    @PutMapping("/cache/{recruit_id}")
    public ResponseEntity<?> editRecruitCache(@RequestBody RecruitReq req, @PathVariable Long recruit_id){
        RecruitRes recruitRes = recruitService.editRecruitCache(req, recruit_id);
        return response.success(recruitRes);
    }

    /*===========================
        채용폼 등록
    ===========================*/
    @PostMapping
    public ResponseEntity<?> registRecruit(@RequestBody RecruitReq req,
                                                 Authentication authentication){
        AuthDTO memberInfo = (AuthDTO) authentication.getPrincipal();
        return recruitService.registRecruit(req, memberInfo.getMemberEmail());
    }

    /*===========================
        채용폼 삭제
    ===========================*/
    @PutMapping("/delete/{recruit_id}")
    public ResponseEntity<?> deleteRecruit(@PathVariable Long recruit_id,
                                             Authentication authentication){
        AuthDTO memberInfo = (AuthDTO) authentication.getPrincipal();
        return recruitService.deleteRecruit(recruit_id, memberInfo.getMemberEmail());
    }
}
