package com.finalproject.recruit.service;

import com.finalproject.recruit.dto.Response;
import com.finalproject.recruit.dto.applymanage.ApplyDetailResponseDTO;
import com.finalproject.recruit.dto.applymanage.ApplyResponseDTO;
import com.finalproject.recruit.dto.applymanage.CountAndDateResponseDTO;
import com.finalproject.recruit.entity.Apply;
import com.finalproject.recruit.entity.Recruit;
import com.finalproject.recruit.parameter.ApplyProcedure;
import com.finalproject.recruit.repository.ApplyRepository;
import com.finalproject.recruit.repository.RecruitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApplyManageService {

    private final ApplyRepository applyRepository;
    private final RecruitRepository recruitRepository;
    private final Response response;

    /**
     * 지원자 전체 조회 (선택된 채용공고에 해당하는)
     * @param recruitId
     * @return
     */
    public ResponseEntity<?> findAllApplicants(Long recruitId) {
        Recruit findRecruit = recruitRepository.findByRecruitId(recruitId).get();
        StringTokenizer st = new StringTokenizer(findRecruit.getKeywordStandard(), ","); //채용폼 키워드 분리

        HashSet<String> keywordStandard = new HashSet<>();

        while (st.hasMoreTokens()) { //HashSet에 기준 키워드 저장 (기업 측이 선정한)
            keywordStandard.add(st.nextToken());
        }

        List<ApplyResponseDTO> data = applyRepository.findByRecruitRecruitId(recruitId)
                .stream().map(ApplyResponseDTO::new)
                .collect(Collectors.toList());

        for (ApplyResponseDTO dto : data) {
            int score = 0;
            List<String> applyKeywords = new ArrayList<>();
            st = new StringTokenizer(dto.getKeywords(), ",");

            while (st.hasMoreTokens()) {
                String keyword = st.nextToken();
                applyKeywords.add(keyword); //키워드를 배열로 저장해서 반환
                if (keywordStandard.contains(keyword)) { //기준 키워드와 동일 시 가점 추가
                    score++;
                }
            }

            dto.setScore(score); //가점 반환
            dto.setKeywordList(applyKeywords); //키워드 배열 반환
        }

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

    /**
     * 인재현황 (총 지원자 수, 오늘 지원자 수, 현재 채용폼의 채용단계, 채용단계마감 디데이)
     * @param recruitId
     * @return
     */
    public ResponseEntity<?> countApplicantAndProcessAndTime(Long recruitId) {
        CountAndDateResponseDTO result = new CountAndDateResponseDTO();

        Long totalCount = applyRepository.countApplicantByRecruitId(recruitId); //총 지원자 수 계산
        result.setTotalCount(totalCount);

        LocalDateTime now = LocalDateTime.now();
        Long todayCount = applyRepository.countApplicantByRecruitIdAndCreatedTime(recruitId, now.toLocalDate()); // 오늘 지원자 수 계산
        result.setTodayCount(todayCount);

        Recruit findRecruit = recruitRepository.findByRecruitId(recruitId).get();
        if (now.isBefore(findRecruit.getDocsStart())) { //오늘 날짜가 서류 시작일 전이면 채용 시작 전

            result.setProcess("채용 시작 전");

            Period diffDate = Period.between(now.toLocalDate(), findRecruit.getDocsStart().toLocalDate()); //날짜 차이 계산
            result.setProcessFinish("채용 시작까지 " + diffDate.getDays() + "남았습니다.");

        } else if (now.isAfter(findRecruit.getDocsStart()) && now.isBefore(findRecruit.getDocsEnd())) {

            result.setProcess("서류전형");

            Period diffDate = Period.between(now.toLocalDate(), findRecruit.getDocsEnd().toLocalDate()); //날짜 차이 계산
            result.setProcessFinish("서류전형 마감까지 " + diffDate.getDays() + "일 남았습니다.");

        } else if (now.isAfter(findRecruit.getDocsEnd()) && now.isBefore(findRecruit.getMeetStart())) {

            result.setProcess("서류전형 마감");

            Period diffDate = Period.between(now.toLocalDate(), findRecruit.getMeetStart().toLocalDate()); //날짜 차이 계산
            result.setProcessFinish("면접전형 시작까지 " + diffDate.getDays() + "일 남았습니다.");

        } else if (now.isAfter(findRecruit.getMeetStart()) && now.isBefore(findRecruit.getMeetEnd())) {

            result.setProcess("면접 진행 중");

            Period diffDate = Period.between(now.toLocalDate(), findRecruit.getMeetEnd().toLocalDate()); //날짜 차이 계산
            result.setProcessFinish("면접전형 종료까지 " + diffDate.getDays() + "남았습니다.");

        } else if (now.isAfter(findRecruit.getMeetEnd()) && now.isBefore(findRecruit.getConfirmStart())) {

            result.setProcess("면접 종료");

            Period diffDate = Period.between(now.toLocalDate(), findRecruit.getConfirmStart().toLocalDate()); //날짜 차이 계산
            result.setProcessFinish("최종조율까지 " + diffDate.getDays() + "남았습니다.");

        } else if (now.isAfter(findRecruit.getConfirmStart()) && now.isBefore(findRecruit.getConfirmEnd())) {

            result.setProcess("최종 조율 중");

            Period diffDate = Period.between(now.toLocalDate(), findRecruit.getConfirmEnd().toLocalDate()); //날짜 차이 계산
            result.setProcessFinish("최종조율 마감까지 " + diffDate.getDays() + "남았습니다.");

        } else {
            result.setProcess("채용마감");
            result.setProcessFinish("채용이 마감되었습니다.");
        }

        return response.success(result);
    }

    /**
     * 지원자 상세조회
     * @param applyId
     * @return
     */
    public ResponseEntity<?> findApplicantDetail(Long applyId) {
        ApplyDetailResponseDTO data = applyRepository.findJoinByApplyId(applyId).map(ApplyDetailResponseDTO::new).get();

        return response.success(data);
    }

    /**
     * 지원자 코멘트 등록
     * @param applyId
     * @param evaluation
     * @return
     */
    @Transactional
    public ResponseEntity<?> writeEvaluation(Long applyId, String evaluation) {
        try {
            Apply findApply = applyRepository.findJoinByApplyId(applyId).get();
            findApply.writeEvaluation(evaluation);
        } catch (Exception e) {
            return response.fail("지원자 코멘트 등록에 실패하였습니다.");
        }

        return response.success("지원자 코멘트 등록에 성공하였습니다.");
    }

    /**
     * 지원자 찜 등록 / 해제 기능
     * @param applyId
     * @return
     */
    @Transactional
    public ResponseEntity<?> changeWish(Long applyId) {
        try {
            Apply findApply = applyRepository.findJoinByApplyId(applyId).get();
            if (!findApply.isWish()) {
                findApply.changeWish();
            } else {
                findApply.cancelWish();
            }
        } catch (Exception e) {
            return response.fail("지원자 찜 등록 / 해제 실패했습니다.");
        }

        return response.success("지원지 찜 등록 / 해제 성공하였습니다.");
    }

    /**
     * 탈락인재 보관함 이동
     * @param applyId
     * @return
     */
    @Transactional
    public ResponseEntity<?> dropApply(Long applyId) {
        try {
            Apply findApply = applyRepository.findJoinByApplyId(applyId).get();
            if (!findApply.isFailApply()) {
                findApply.changeDrop();
            } else {
                findApply.cancelDrop();
            }
        } catch (Exception e) {
            return response.fail("탈락인재 보관함 등록 / 해제 실패하였습니다.");
        }

        return response.success("탈락인재 보관함 등록 / 해제 성공하였습니다.");
    }

    /**
     * 인재 면접날짜 지정
     * @param applyId
     * @param meeting
     * @return
     */
    @Transactional
    public ResponseEntity<?> setMeetDay(Long applyId, String meeting) {
        try {
            LocalDateTime meetingDay = LocalDateTime.parse(meeting);

            Apply findApply = applyRepository.findJoinByApplyId(applyId).get();
            findApply.setMeeting(meetingDay);
        } catch (Exception e) {
            return response.fail("면접일자 등록에 실패하였습니다.");
        }

        return response.success("면접일자 등록에 성공하였습니다.");
    }

    /**
     * 인재 서류검토 처리
     * @param applyId
     * @return
     */
    @Transactional
    public ResponseEntity<?> checkApplicant(Long applyId) {
        try {
            Apply findApply = applyRepository.findJoinByApplyId(applyId).get();
            findApply.setCheckApply();
        } catch (Exception e) {
            return response.fail("서류검토 처리에 실패하였습니다.");
        }

        return response.success("서류검토 처리에 성공하였습니다.");
    }
}
