package com.finalproject.recruit.service;

import com.finalproject.recruit.dto.Response;
import com.finalproject.recruit.dto.applymanage.ApplyResponseDTO;
import com.finalproject.recruit.repository.ApplyRepository;
import com.finalproject.recruit.repository.RecruitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApplyManageService {

    private final ApplyRepository applyRepository;
    private final Response response;

    /**
     * 지원자 전체 조회 (선택된 채용공고에 해당하는)
     * @param recruitId
     * @return
     */
    public ResponseEntity<?> findAllApplicants(Long recruitId) {
        List<ApplyResponseDTO> data = applyRepository.findByRecruitRecruitId(recruitId)
                .stream().map(ApplyResponseDTO::new)
                .collect(Collectors.toList());

        return response.success(data);
    }
}
