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
import com.finalproject.recruit.parameter.NoticeStep;
import com.finalproject.recruit.repository.ApplyRepository;
import com.finalproject.recruit.repository.MailRepository;
import com.finalproject.recruit.repository.RecruitRepository;
import lombok.RequiredArgsConstructor;
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

    public ResponseEntity<?> recruitList(String memberEmail) {
        List<NoticeRecruitsRes> noticeRecruitsRes = recruitRepository.findByMemberMemberEmail(memberEmail).stream()
                .map(NoticeRecruitsRes::fromRecruit)
                .collect(Collectors.toList());
        return response.success(noticeRecruitsRes);
    }

    public ResponseEntity<?> applicantList(Long recruitId) {

        List<ApplicantsRes> applicantsRes = applyRepository.findByRecruitRecruitId(recruitId)
                .stream()
                .map(apply -> ApplicantsRes.fromApply(
                        apply,
                        mailRepository.findTopByApplyApplyIdOrderByCreatedTimeDesc(apply.getApplyId())
                                .map(Mail::getCreatedTime)
                                .orElse(null)))
                .collect(Collectors.toList());
        return response.success(applicantsRes);

    }

    public ResponseEntity<?> messageHistory(Long recruitId) {
        List<MessageHistoryRes> messageHistoryRes = mailRepository.findByRecruitRecruitId(recruitId)
                .stream()
                .map(mail -> MessageHistoryRes.fromEntity(
                        mail,
                        mail.getApply()
                ))
                .collect(Collectors.toList());
        return response.success(messageHistoryRes);
    }

    @Transactional
    public ResponseEntity<?> sendEmail(EmailReq emailReq) {
        try {
            Recruit recruit = recruitRepository.findByRecruitIdAndRecruitDeleteIsFalse(emailReq.getRecruitId()).orElseThrow(()-> new IllegalArgumentException("해당 채용공고가 존재하지 않습니다."));
            String message=emailReq.getMailContent(); //보낼 메시지
            String interviewDate = emailReq.getInterviewDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd일 HH시 mm분"));
            if (emailReq.getNoticeStep() == NoticeStep.면접제안) {
                message+="면접 날짜는 "+ interviewDate+"입니다. "+
                    "잘 준비하셔서 좋은 성과 있으시길 바랍니다. ";
            }
            message+="감사합니다. ";


            //이메일 전송!!
            List<String> applyEmails = emailReq.getApplyIds().stream()
                    .map(applyId -> applyRepository.findByApplyId(applyId).orElseThrow(()-> new IllegalArgumentException("해당 apply존재하지 않습니다.")))
                    .map(Apply::getApplyEmail)
                    .collect(Collectors.toList());

            String recipient = String.join(" ",applyEmails);
            String title = recruit.getMember().getCompanyName()+"입니다.";
            SimpleMailMessage notice = new SimpleMailMessage();
            notice.setTo(recipient.split(" "));
            notice.setSubject(title);
            notice.setText(message);
            mailSender.send(notice);


            //이메일 전송 내역 저장
            List<Apply> applies = applyEmails.stream()
                        .map(email -> applyRepository.findByApplyEmail(email).orElse(null))
                        .collect(Collectors.toList());

            for(Apply a : applies){
                Mail mail = Mail.builder()
                        .recruit(recruit)
                        .apply(a)
                        .mailContent(message)
                        .noticeStep(emailReq.getNoticeStep())
                        .build();
                mailRepository.save(mail);
            }

            return response.success("이메일이 전송되었습니다.");

        } catch (Exception e){
            return response.fail("이메일 전송에 실패하였습니다.");
        }
    }

    public ResponseEntity<?> selectStep(SelectStepReq selectStepReq) {
        Recruit recruit = recruitRepository.findByRecruitIdAndRecruitDeleteIsFalse(selectStepReq.getRecruitId())
                .orElseThrow(() -> new IllegalArgumentException("해당 채용공고가 존재하지 않습니다."));

        String message = "안녕하세요. "+ recruit.getMember().getCompanyName()+"입니다. "+
                "저희 "+ recruit.getRecruitTitle()+"에 관심을 가지고 지원해 주셔서 감사합니다. ";
        message+=selectStepReq.getNoticeStep().getMessage();

        return response.success(message, "success", HttpStatus.OK);
    }
}
