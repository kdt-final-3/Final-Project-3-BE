package com.finalproject.recruit.service;

import com.finalproject.recruit.dto.Response;
import com.finalproject.recruit.dto.applymanage.ApplyResponseDTO;
import com.finalproject.recruit.entity.Apply;
import com.finalproject.recruit.parameter.ApplyProcedure;
import com.finalproject.recruit.repository.ApplyRepository;
import com.finalproject.recruit.repository.RecruitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    /**
     * 채용단계별 지원자 조회
     * @param recruitId
     * @param procedure
     * @return
     */
    public ResponseEntity<?> findApplicantByProcedure(Long recruitId, String procedure) {
        ApplyProcedure applyProcedure = Enum.valueOf(ApplyProcedure.class, procedure);

        List<ApplyResponseDTO> data = applyRepository.findByRecruit_RecruitIdAndApplyProcedure(recruitId, applyProcedure)
                .stream().map(ApplyResponseDTO::new)
                .collect(Collectors.toList());

        return response.success(data);
    }

    /**
     * 지원자 채용단계 수정
     * @param applyId
     * @param procedure
     * @return
     */
    @Transactional
    public ResponseEntity<?> changeApplyProcedure(Long applyId, String procedure) {
        try {
            ApplyProcedure applyProcedure = Enum.valueOf(ApplyProcedure.class, procedure);

            Apply findApply = applyRepository.findByApplyId(applyId).get();
            findApply.changeProcedure(applyProcedure);
        } catch (Exception e) {
            return response.fail("채용단계 수정에 실패하였습니다.");
        }

        return response.success("채용단계 수정에 성공하였습니다.");
    }

    /**
     * 지원자 합격 또는 불합격 처리
     * @param applyId
     * @return
     */
    @Transactional
    public ResponseEntity<?> changePassOrFail(Long applyId) {
        try {
            Apply findApply = applyRepository.findByApplyId(applyId).get();

            if (!findApply.isPass()) {
                findApply.changePass();
            } else {
                findApply.cancelPass();
            }
        } catch (Exception e) {
            return response.fail("지원자 합격 / 불합격 처리에 실패했습니다.");
        }

        return response.success("지원자 합격 / 불합격 처리에 성공하였습니다.");
    }
}
