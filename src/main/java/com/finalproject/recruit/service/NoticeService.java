package com.finalproject.recruit.service;

import com.finalproject.recruit.dto.Response;
import com.finalproject.recruit.dto.keep.ApplicantsRes;
import com.finalproject.recruit.dto.notice.EmailReq;
import com.finalproject.recruit.dto.notice.MessageHistoryRes;
import com.finalproject.recruit.dto.notice.NoticeRecruitsRes;
import com.finalproject.recruit.dto.notice.SelectStepReq;
import com.finalproject.recruit.entity.Apply;
import com.finalproject.recruit.entity.Recruit;
import com.finalproject.recruit.entity.Mail;
import com.finalproject.recruit.exception.notice.ErrorCode;
import com.finalproject.recruit.exception.notice.NoticeException;
import com.finalproject.recruit.parameter.NoticeStep;
import com.finalproject.recruit.repository.ApplyRepository;
import com.finalproject.recruit.repository.MailRepository;
import com.finalproject.recruit.repository.RecruitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final ApplyRepository applyRepository;
    private final RecruitRepository recruitRepository;
    private final MailRepository mailRepository;

    private final Response response;
    private final JavaMailSender mailSender;


    /*===========================
        Mail 전송대상(채용폼) 조회
    ===========================*/
    public ResponseEntity<?> recruitList(String memberEmail) {
        try{
            List<NoticeRecruitsRes> noticeRecruitsRes = recruitRepository.findByMemberMemberEmail(memberEmail).stream()
                    .map(NoticeRecruitsRes::fromRecruit)
                    .collect(Collectors.toList());

            // empty check
            if(!hasContents(noticeRecruitsRes)){
                return response.fail(
                        ErrorCode.RECRUIT_FORM_NOT_FOUND.getMessage(),
                        ErrorCode.RECRUIT_FORM_NOT_FOUND.getStatus());
            }
            return response.success(
                    noticeRecruitsRes,
                    "Successfully Get RecruitForms"
            );

        }catch(NoticeException e){
            e.printStackTrace();
            throw new NoticeException(ErrorCode.UNABLE_TO_PROCESS_REQUEST);
        }
    }

    /*===========================
        Mail 전송대상(인재) 조회
    ===========================*/
    public ResponseEntity<?> applicantList(Long recruitId, Pageable pageable) {
        try{
            Page<ApplicantsRes> applicantsRes = applyRepository.findByRecruitRecruitId(recruitId, pageable)
                    .map(apply -> ApplicantsRes.fromApply(
                            apply,
                            mailRepository.findTopByApplyApplyIdOrderByCreatedTimeDesc(apply.getApplyId())
                                    .map(Mail::getCreatedTime)
                                    .orElse(null)));
            // empty check
            if(!hasApplicants(applicantsRes)){
                return response.fail(
                        ErrorCode.APPLICANTS_NOT_FOUND.getMessage(),
                        ErrorCode.APPLICANTS_NOT_FOUND.getStatus());
            }
            return response.success(
                    applicantsRes,
                    "Successfully Get Applications"
                    );

        }catch(NoticeException e){
            e.printStackTrace();
            throw new NoticeException(ErrorCode.UNABLE_TO_PROCESS_REQUEST);
        }
    }

    /*===========================
        Mail 전송내역 조회
    ===========================*/
    public ResponseEntity<?> messageHistory(Long recruitId) {
        try{
            List<MessageHistoryRes> messageHistoryRes = mailRepository.findByRecruitRecruitId(recruitId)
                    .stream()
                    .map(mail -> MessageHistoryRes.fromEntity(
                            mail,
                            mail.getApply()))
                    .collect(Collectors.toList());

            if(!hasContents(messageHistoryRes)){
                return response.fail(
                        ErrorCode.HISTORY_NOT_FOUND.getMessage(),
                        ErrorCode.HISTORY_NOT_FOUND.getStatus());
            }
            return response.success(
                    messageHistoryRes,
                    "Successfully Get MessageHistory"
            );
        }catch(NoticeException e){
            e.printStackTrace();
            throw new NoticeException(ErrorCode.UNABLE_TO_PROCESS_REQUEST);
        }
    }

    /*===========================
        Mail 전송
    ===========================*/
    @Transactional
    public ResponseEntity<?> sendEmail(EmailReq emailReq) {
        try {
            Recruit recruit = recruitRepository.findByRecruitId(emailReq.getRecruitId()).orElseThrow(
                    ()-> new NoticeException(ErrorCode.RECRUIT_FORM_NOT_FOUND));

            List<String> applyEmails = emailReq.getApplyIds().stream()
                    .map(applyId -> applyRepository.findByApplyId(applyId).orElseThrow(
                            ()-> new NoticeException(ErrorCode.APPLY_NOT_FOUND)))
                    .map(Apply::getApplyEmail)
                    .collect(Collectors.toList());

            // empty check
            if(!hasContents(applyEmails)){
                return response.fail(
                        ErrorCode.APPLICANTS_NOT_FOUND.getMessage(),
                        ErrorCode.APPLY_NOT_FOUND.getStatus());
            }
            String message = msgGenerate(emailReq);

            // Email Sending
            mailSend(recruit, applyEmails, message);

            // Sending History Archiving
            archiveSendHistory(recruit, applyEmails, message, emailReq.getNoticeStep());

            return response.success("Successfully Sending Email");

        } catch (NoticeException e) {
            e.printStackTrace();
            throw new NoticeException(ErrorCode.UNABLE_TO_SEND_MESSAGE);
        }
    }

    // 사전 메세지 생성 & NoticeStep 확인
    public String msgGenerate(EmailReq req){
        String msg = req.getMailContent();
        String interviewDate = req.getInterviewDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd일 HH시 mm분"));

        // Step check
        if(req.getNoticeStep() == null){
            throw new NoticeException(ErrorCode.INVALID_STEP);
        }else if (req.getNoticeStep().name().equals(NoticeStep.MEET_PROPOSAL.name())) {
            msg += " 면접 날짜는 "+ interviewDate + "입니다. " +
                    "잘 준비하셔서 좋은 성과 있으시길 바랍니다. ";
        }
        msg += "감사합니다. ";
        return msg;
    }

    // 이메일 발송
    public void mailSend(Recruit recruit, List<String> applyEmails, String msg){
        String recipient = String.join(" ",applyEmails);
        String title = recruit.getMember().getCompanyName()+" 입니다.";

        SimpleMailMessage notice = new SimpleMailMessage();
        notice.setTo(recipient.split(" "));
        notice.setSubject(title);
        notice.setText(msg);

        try{
            mailSender.send(notice);
        }catch (NoticeException e){
            e.printStackTrace();
            throw new NoticeException(ErrorCode.UNABLE_TO_SEND_MESSAGE);
        }
    }

    // 이메일 전송내역 저장
    @Transactional
    public void archiveSendHistory(Recruit recruit, List<String> applyEmails, String msg, NoticeStep step){
        List<Apply> applies = applyEmails.stream()
                .map(email -> applyRepository.findByApplyEmail(email).orElse(null))
                .collect(Collectors.toList());

        // empty check
        if(!hasContents(applies)){
            throw new NoticeException(ErrorCode.APPLY_NOT_FOUND);
        }
        try{
            for(Apply a : applies){
                Mail mail = Mail.builder()
                        .recruit(recruit)
                        .apply(a)
                        .mailContent(msg)
                        .noticeStep(step)
                        .build();
                mailRepository.save(mail);
            }
        }catch (NoticeException e){
            e.printStackTrace();
            throw new NoticeException(ErrorCode.UNABLE_TO_ARCHIVE_HISTORY);
        }
    }

    /*===========================
        Mail 전송 템플릿
    ===========================*/
    public ResponseEntity<?> selectStep(SelectStepReq selectStepReq) {
        Recruit recruit = recruitRepository.findByRecruitId(selectStepReq.getRecruitId())
                .orElseThrow(() -> new NoticeException(ErrorCode.RECRUIT_FORM_NOT_FOUND));

        String message =
                "안녕하세요. " + recruit.getMember().getCompanyName() + "입니다. " +
                "저희 " + recruit.getRecruitTitle() + "에 관심을 가지고 지원해 주셔서 감사합니다. ";

        message += selectStepReq.getNoticeStep().getMessage();

        return response.success(
                message,
                "Successfully Create Selected StepMessage",
                HttpStatus.OK);
    }

    /*===========================
        Mail 전송대상(인재) 검색
    ===========================*/
    @Transactional
    public ResponseEntity<?> searchApplicant(String applyName, Long recruitId) {
        try{
            List<Apply> applies = applyRepository.findByApplyNameAndRecruitRecruitId(applyName, recruitId);
            // empty check
            if(!hasContents(applies)){
                return response.fail(
                        ErrorCode.APPLY_NOT_FOUND.getMessage(),
                        ErrorCode.APPLY_NOT_FOUND.getStatus());
            }

            List<ApplicantsRes> applicantsResList = applies.stream()
                    .map(apply -> ApplicantsRes.fromApply(
                            apply,
                            mailRepository.findTopByApplyApplyIdOrderByCreatedTimeDesc(apply.getApplyId())
                                    .map(Mail::getCreatedTime)
                                    .orElse(null)))
                    .collect(Collectors.toList());
            // empty check
            if(hasContents(applicantsResList)){
                return response.fail(
                        ErrorCode.APPLICANTS_NOT_FOUND.getMessage(),
                        ErrorCode.APPLICANTS_NOT_FOUND.getStatus());
            }

            return response.success(
                    applicantsResList,
                    "Successfully Get Applicants");
        }catch(NoticeException e){
            e.printStackTrace();
            throw new NoticeException(ErrorCode.UNABLE_TO_PROCESS_REQUEST);
        }
    }

    /*===========================
        Empty Check
    ===========================*/
    public boolean hasApplicants(Page<?> input){
        return input.hasContent();
    }

    public boolean hasContents(List<?> input){
        return (input != null && !input.isEmpty());
    }


}
