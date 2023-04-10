package com.finalproject.recruit.service;

import com.finalproject.recruit.dto.Response;
import com.finalproject.recruit.dto.applymanage.*;
import com.finalproject.recruit.entity.Apply;
import com.finalproject.recruit.entity.Recruit;
import com.finalproject.recruit.exception.applyManage.ApplyManageException;
import com.finalproject.recruit.exception.applyManage.ErrorCode;
import com.finalproject.recruit.parameter.ApplyProcedure;
import com.finalproject.recruit.repository.ApplyRepository;
import com.finalproject.recruit.repository.RecruitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApplyManageService {

    private final ApplyRepository applyRepository;
    private final RecruitRepository recruitRepository;
    private final Response response;

    /*===========================
      지원자 전체 조회 (선택된 채용공고에 해당하는)
    ===========================
    * @param recruitId
    * @return
    */
    public ResponseEntity<?> findAllApplicants(Long recruitId) {
        try{
            // 채용폼 키워드 추출
            HashSet<String> keywordStandard = extractKeywordFromRecruitForm(recruitId);

            // 인재 지원서 추출
            List<ApplyResponseDTO> data = applyRepository.findByRecruit_RecruitId(recruitId)
                    .stream().map(ApplyResponseDTO::new)
                    .collect(Collectors.toList());

            if (data.isEmpty()) {
                return response.fail(
                        ErrorCode.APPLICANTS_NOT_FOUND.getMessage(),
                        ErrorCode.APPLICANTS_NOT_FOUND.getStatus());
            }

            // 키워드 연산
            for (ApplyResponseDTO dto : data) {
                calcKeywordScore(dto, keywordStandard);
            }

            return response.success(data, "SuccessFully FindAll Applicants");

        } catch(ApplyManageException e){
            e.printStackTrace();
            throw new ApplyManageException(ErrorCode.APPLICANTS_FOUND_FAILED);
        }
    }

    /**
     * 채용단계별 지원자 조회
     * @param recruitId
     * @param procedure
     * @return
     */
    public ResponseEntity<?> findApplicantByProcedure(Long recruitId, String procedure) {
        try {

            ApplyProcedure applyProcedure = Enum.valueOf(ApplyProcedure.class, procedure);

            List<ApplyResponseDTO> data = applyRepository.findByRecruit_RecruitIdAndApplyProcedure(recruitId, applyProcedure)
                    .stream().map(ApplyResponseDTO::new)
                    .collect(Collectors.toList());

            if (data.isEmpty()) {
                return response.fail(
                        ErrorCode.APPLICANTS_NOT_FOUND.getMessage(),
                        ErrorCode.APPLICANTS_NOT_FOUND.getStatus());
            }

            //채용폼 키워드 추출
            HashSet<String> keywordStandard = extractKeywordFromRecruitForm(recruitId);

            // 키워드 연산
            for (ApplyResponseDTO dto : data) {
                calcKeywordScore(dto, keywordStandard);
            }

            return response.success(data);

        } catch (ApplyManageException e) {
            e.printStackTrace();
            throw new ApplyManageException(ErrorCode.APPLICANTS_FOUND_FAILED);
        }
    }

    public HashSet<String> extractKeywordFromRecruitForm(Long recruitId){
        try{
            // 채용폼 키워드 추출
            Recruit findRecruit = recruitRepository.findByRecruitId(recruitId).orElseThrow(
                    () -> new ApplyManageException(ErrorCode.RECRUIT_FORM_NOT_FOUND));
            StringTokenizer st = new StringTokenizer(findRecruit.getKeywordStandard(), ","); //채용폼 키워드 분리
            HashSet<String> keywordStandard = new HashSet<>();

            // HashSet에 기준 키워드 저장 (기업 측이 선정한)
            while (st.hasMoreTokens()) {
                keywordStandard.add(st.nextToken());
            }

            return keywordStandard;

        } catch(ApplyManageException e) {
            e.printStackTrace();
            throw new ApplyManageException(ErrorCode.RECRUIT_FROM_KEYWORD_EXTRACTION_FAILED);
        }
    }

    public void calcKeywordScore(ApplyResponseDTO dto, HashSet<String> keywordStandard){
        try{
            //키워드를 배열로 저장해서 반환
            List<String> applyKeywords = Arrays.asList(dto.getKeywords().split(","));

            //기준 키워드와 동일 시 가점 추가
            int score = (int) applyKeywords.stream().filter(keywordStandard::contains).count();

            dto.setScore(score);                //가점 반환
            dto.setKeywordList(applyKeywords);  //키워드 배열 반환

        }catch(Exception e){
            e.printStackTrace();
            throw new ApplyManageException(
                    ErrorCode.KEYWORD_SCORE_CALCULATION_FAILED,
                    String.format("ApplyId : %s - %s", dto.getApplyId(),
                            ErrorCode.KEYWORD_SCORE_CALCULATION_FAILED.getMessage())
            );
        }
    }

    /* Archive
    int score = 0;
    List<String> applyKeywords = new ArrayList<>();
    StringTokenizer st = new StringTokenizer(dto.getKeywords(), ",");

    while (st.hasMoreTokens()) {
        String keyword = st.nextToken();
        applyKeywords.add(keyword); //키워드를 배열로 저장해서 반환
        if (keywordStandard.contains(keyword)) { //기준 키워드와 동일 시 가점 추가
            score++;
        }
    }
    */


    /**
     * 지원자 채용단계 수정
     * @param applyProcedureReq
     * @return
     */
    @Transactional
    public ResponseEntity<?> changeApplyProcedure(ApplyProcedureReq applyProcedureReq) {
        try {

            ApplyProcedure applyProcedure = Enum.valueOf(ApplyProcedure.class, applyProcedureReq.getProcedure());
            applyRepository.updateApplicantProcedure(applyProcedure, applyProcedureReq.getApplyIds());

        } catch (ApplyManageException e) {
            e.printStackTrace();
            return response.fail(
                    ErrorCode.FAIL_CHANGE_APPLICANTS_PROCEDURE.getMessage(),
                    ErrorCode.FAIL_CHANGE_APPLICANTS_PROCEDURE.getStatus());
        }

        return response.success("Success to change applicant procedure!");
    }

    /**
     * 지원자 합격 또는 불합격 처리
     * @param applyId
     * @return
     */
    @Transactional
    public ResponseEntity<?> changePassOrFail(Long applyId) {
        try {

            Apply findApply = applyRepository.findByApplyId(applyId).orElseThrow(
                    () -> new ApplyManageException(ErrorCode.APPLICANTS_NOT_FOUND)
            );

            if (!findApply.isPass()) findApply.changePass();
            else findApply.cancelPass();

        } catch (ApplyManageException e) {
            e.printStackTrace();
            return response.fail(
                    ErrorCode.FAIL_CHANGE_APPLICANT_PASS_OR_FAIL.getMessage(),
                    ErrorCode.FAIL_CHANGE_APPLICANT_PASS_OR_FAIL.getStatus()
            );
        }

        return response.success("Success to change applicant Pass or Fail!");
    }

    /**
     * 인재현황 (총 지원자 수, 오늘 지원자 수, 현재 채용폼의 채용단계, 채용단계마감 디데이)
     * @param recruitId
     * @return
     */
    public ResponseEntity<?> countApplicantAndProcessAndTime(Long recruitId) {
        try {
            CountAndDateResponseDTO result = new CountAndDateResponseDTO();

            Long totalCount = applyRepository.countApplicantByRecruitId(recruitId); //총 지원자 수 계산
            result.setTotalCount(totalCount);

            LocalDateTime now = LocalDateTime.now();
            Long todayCount = applyRepository.countApplicantByRecruitIdAndCreatedTime(recruitId, now.toLocalDate()); // 오늘 지원자 수 계산
            result.setTodayCount(todayCount);

            Recruit findRecruit = recruitRepository.findByRecruitId(recruitId).orElseThrow(
                    () -> new ApplyManageException(ErrorCode.RECRUIT_FORM_NOT_FOUND)
            );

            if (now.isBefore(findRecruit.getDocsStart())) { //오늘 날짜가 서류 시작일 전이면 채용 시작 전

                result.setProcess("채용 시작 전");

                Period diffDate = Period.between(now.toLocalDate(), findRecruit.getDocsStart().toLocalDate()); //날짜 차이 계산
                result.setProcessFinish("채용 시작까지 " + diffDate.getDays() + "일 남았습니다.");

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
                result.setProcessFinish("면접전형 종료까지 " + diffDate.getDays() + "일 남았습니다.");

            } else if (now.isAfter(findRecruit.getMeetEnd()) && now.isBefore(findRecruit.getConfirmStart())) {

                result.setProcess("면접 종료");

                Period diffDate = Period.between(now.toLocalDate(), findRecruit.getConfirmStart().toLocalDate()); //날짜 차이 계산
                result.setProcessFinish("최종조율까지 " + diffDate.getDays() + "일 남았습니다.");

            } else if (now.isAfter(findRecruit.getConfirmStart()) && now.isBefore(findRecruit.getConfirmEnd())) {

                result.setProcess("최종 조율 중");

                Period diffDate = Period.between(now.toLocalDate(), findRecruit.getConfirmEnd().toLocalDate()); //날짜 차이 계산
                result.setProcessFinish("최종조율 마감까지 " + diffDate.getDays() + "일 남았습니다.");

            } else {
                result.setProcess("채용마감");
                result.setProcessFinish("채용이 마감되었습니다.");
            }

            return response.success(result);

        } catch (ApplyManageException e) {
            e.printStackTrace();
            throw new ApplyManageException(ErrorCode.FAIL_COUNT_APPLICANT_AND_CHECK_PROCESS_AND_DAY);
        }
    }

    /**
     * 지원자 상세조회
     * @param applyId
     * @return
     */
    public ResponseEntity<?> findApplicantDetail(Long applyId) {
        ApplyDetailResponseDTO data = applyRepository.findJoinByApplyId(applyId).map(ApplyDetailResponseDTO::new).orElseThrow(
                () -> new ApplyManageException(ErrorCode.FAIL_CHECK_APPLICANT_DETAIL)
        );

        //키워드를 배열로 저장해서 반환
        List<String> applyKeywords = Arrays.asList(data.getKeywords().split(","));
        data.setKeywordList(applyKeywords);

        return response.success(data);
    }

    /**
     * 지원자 코멘트 등록
     * @param applyId
     * @param evaluationReq
     * @return
     */
    @Transactional
    public ResponseEntity<?> writeEvaluation(Long applyId, EvaluationReq evaluationReq) {
        try {

            Apply findApply = applyRepository.findJoinByApplyId(applyId).orElseThrow(
                    () -> new ApplyManageException(ErrorCode.APPLICANTS_NOT_FOUND)
            );

            findApply.writeEvaluation(evaluationReq.getEvaluation());

        } catch (ApplyManageException e) {
            e.printStackTrace();
            return response.fail(
                    ErrorCode.FAIL_COMMENT_APPLICANT.getMessage(),
                    ErrorCode.FAIL_COMMENT_APPLICANT.getStatus()
            );
        }

        return response.success("Success to Comment Applicant!");
    }

    /**
     * 지원자 찜 등록 / 해제 기능
     * @param applyId
     * @return
     */
    @Transactional
    public ResponseEntity<?> changeWish(Long applyId) {
        try {

            Apply findApply = applyRepository.findJoinByApplyId(applyId).orElseThrow(
                    () -> new ApplyManageException(ErrorCode.APPLICANTS_NOT_FOUND)
            );

            if (!findApply.isWish()) findApply.changeWish();
            else findApply.cancelWish();

        } catch (ApplyManageException e) {
            return response.fail(
                    ErrorCode.FAIL_WISH_APPLICANT.getMessage(),
                    ErrorCode.FAIL_WISH_APPLICANT.getStatus()
            );
        }

        return response.success("Success to Wish / Cancel wish Applicant!");
    }

    /**
     * 탈락인재 보관함 이동
     * @param applyId
     * @return
     */
    @Transactional
    public ResponseEntity<?> dropApply(Long applyId) {
        try {

            Apply findApply = applyRepository.findJoinByApplyId(applyId).orElseThrow(
                    () -> new ApplyManageException(ErrorCode.APPLICANTS_NOT_FOUND)
            );

            if (!findApply.isFailApply()) findApply.changeDrop();
            else findApply.cancelDrop();

        } catch (ApplyManageException e) {
            return response.fail(
                    ErrorCode.FAIL_DROP_APPLICANT.getMessage(),
                    ErrorCode.FAIL_DROP_APPLICANT.getStatus()
            );
        }

        return response.success("Success to Move / Cancel Applicant in DropBox!");
    }

    /**
     * 탈락인재 보관함 이동 (일괄처리)
     * @param applyIdsReq
     * @return
     */
    @Transactional
    public ResponseEntity<?> dropApplicants(ApplyIdsReq applyIdsReq) {
        try {

            applyRepository.updateApplicantsDrop(applyIdsReq.getApplyIds());

        } catch (ApplyManageException e) {
            return response.fail(
                    ErrorCode.FAIL_DROP_APPLICANT.getMessage(),
                    ErrorCode.FAIL_DROP_APPLICANT.getStatus()
            );
        }

        return response.success("Success to Move Applicants in DropBox!");
    }


    /**
     * 인재 면접날짜 지정
     * @param applyId
     * @param meetingDateReq
     * @return
     */
    @Transactional
    public ResponseEntity<?> setMeetDay(Long applyId, MeetingDateReq meetingDateReq) {
        try {

            String meeting = meetingDateReq.getInterviewDate() + "T" + meetingDateReq.getInterviewTime();
            LocalDateTime meetingDay = LocalDateTime.parse(meeting);

            Apply findApply = applyRepository.findJoinByApplyId(applyId).orElseThrow(
                    () -> new ApplyManageException(ErrorCode.APPLICANTS_NOT_FOUND)
            );

            findApply.setMeeting(meetingDay);

        } catch (Exception e) {
            return response.fail(
                    ErrorCode.FAIL_SET_APPLICANT_MEETING_DAY.getMessage(),
                    ErrorCode.FAIL_SET_APPLICANT_MEETING_DAY.getStatus()
            );
        }

        return response.success("Success to Set Applicant Meeting Day!");
    }

    /**
     * 인재 서류검토 처리
     * @param applyId
     * @return
     */
    @Transactional
    public ResponseEntity<?> checkApplicant(Long applyId) {
        try {

            Apply findApply = applyRepository.findJoinByApplyId(applyId).orElseThrow(
                    () -> new ApplyManageException(ErrorCode.APPLICANTS_NOT_FOUND)
            );

            findApply.setCheckApply();

        } catch (Exception e) {
            return response.fail(
                    ErrorCode.FAIL_SET_CHECKING_APPLY_DOCS.getMessage(),
                    ErrorCode.FAIL_SET_CHECKING_APPLY_DOCS.getStatus()
            );
        }

        return response.success("Success to Set Checking Applicant Document!");
    }
}
