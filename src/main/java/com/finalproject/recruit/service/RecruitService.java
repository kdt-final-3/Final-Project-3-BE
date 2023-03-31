package com.finalproject.recruit.service;

import com.finalproject.recruit.dto.recruit.RecruitRes;
import com.finalproject.recruit.entity.Recruit;
import com.finalproject.recruit.repository.RecruitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecruitService {
    private final RecruitRepository recruitRepository;

    /*===========================
        채용폼 전체조회
    ===========================*/
    public List<RecruitRes> selectALlRecruit(String memberId, boolean recruitStatus) {
        try {
            List<Recruit> recruitList = recruitRepository.findAllByRecruitOngoing(recruitStatus);
            return checkRecruitRes(recruitList);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /*===========================
        채용폼 검색
    ===========================*/
    public List<RecruitRes> searchRecruit(String memberId, boolean recruitStatus, String recruitTitle){
        try{
            List<Recruit> recruitList = recruitRepository.findAllByRecruitOngoingAndRecruitTitleContains(recruitStatus, recruitTitle);
            return checkRecruitRes(recruitList);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    // 조회결과가 비어있는지 확인하는 메소드
    private List<RecruitRes> checkRecruitRes(List<Recruit> recruitList) {
        if(recruitList.size() == 0){ return null; }
        List<RecruitRes> recruitResList = new ArrayList<>();

        for (Recruit recruit : recruitList) {
            recruitResList.add(new RecruitRes(recruit));
        }

        return recruitResList;
    }
}
