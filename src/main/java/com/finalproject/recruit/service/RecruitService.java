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
            List<RecruitRes> recruitResList = new ArrayList<>();

            for (Recruit recruit : recruitList) {
                recruitResList.add(new RecruitRes(recruit));
            }

            return recruitResList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
